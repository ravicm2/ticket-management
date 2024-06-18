package com.ticket.management.entity;

import com.ticket.management.enums.Section;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Ticket")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "boarding_station", nullable = false)
    private String boardingStation;

    @Column(name = "destination_station", nullable = false)
    private String destinationStation;

    @Column(name = "ticket_price", nullable = false)
    @Setter(AccessLevel.NONE)
    private Integer ticketPrice = 5;

    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private SeatEntity seat;

    /*UserEntity user = new UserEntity();
        user.setFirstName("");
        user.setLastName("");
        user.setEmail("");
        user.setPhoneNumber("");
        user.setId(1L);
    SeatEntity seat = new SeatEntity();
        seat.setIsBooked(true);
        seat.setSeatNumber("1A");
        seat.setSection(Section.A);
        seat.setId(1L);
    TicketEntity validTicket = new TicketEntity();
        validTicket.setId(1L);
        validTicket.setUser(user);
        validTicket.setSeat(seat);*/
}
