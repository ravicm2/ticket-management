package com.ticket.management.entity;

import jakarta.persistence.*;
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
public class TicketEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "boarding_station", nullable = false)
    private String boardingStation;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "price_paid", nullable = false)
    private Integer pricePaid;

    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @OneToOne(mappedBy = "ticket")
    private SeatEntity seat;
}
