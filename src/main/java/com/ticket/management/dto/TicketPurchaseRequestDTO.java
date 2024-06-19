package com.ticket.management.dto;

import com.ticket.management.enums.Section;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketPurchaseRequestDTO {

    private UserDTO user;
    private String fromLocation;
    private String toLocation;
    private LocalDate travelDate;
    private String section;
    private int quantity;

}
