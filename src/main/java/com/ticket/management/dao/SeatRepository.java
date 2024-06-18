package com.ticket.management.dao;

import com.ticket.management.entity.SeatEntity;
import com.ticket.management.enums.Section;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends CrudRepository<SeatEntity, Long> {

    List<SeatEntity> findBySection(Section section);

    Optional<SeatEntity> findBySectionAndSeatNumber(Section section, String seatNumber);

    long countBySectionAndIsBookedFalse(Section section);
}
