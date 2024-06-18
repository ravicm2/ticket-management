package com.ticket.management.entity;

import com.ticket.management.enums.Section;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Seat")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "section", nullable = false)
    private Section section;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(name = "is_booked")
    private Boolean isBooked;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticket;

}
