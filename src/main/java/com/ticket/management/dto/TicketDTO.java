package com.ticket.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {

    private Long id;

    private UserDTO userDTO;

    private String boardingStation;

    private String destinationStation;

    private Integer price;

    private LocalDate travelDate;

    private LocalDate bookingDate;

    private SeatDTO seatDTO;

}
