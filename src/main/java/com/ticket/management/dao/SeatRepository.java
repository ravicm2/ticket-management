package com.ticket.management.dao;

import com.ticket.management.entity.SeatEntity;
import com.ticket.management.enums.Section;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends CrudRepository<SeatEntity, Long> {

    List<SeatEntity> findBySection(Section section);

    Optional<SeatEntity> findBySectionAndSeatNumber(Section section, String seatNumber);

    long countBySectionAndIsBookedFalse(Section section);

    @Query(value = "SELECT id, created_Date, updated_Date, section, seat_number, is_booked, ticket_id FROM Seat s WHERE s.section = :section AND s.is_booked = false ORDER BY s.seat_number ASC", nativeQuery = true)
    List<SeatEntity> findAvailableSeats(@Param("section") String section, Pageable pageable);
}
