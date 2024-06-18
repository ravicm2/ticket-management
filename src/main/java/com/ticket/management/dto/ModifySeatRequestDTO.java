package com.ticket.management.dto;

import com.ticket.management.enums.Section;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModifySeatRequestDTO {

    private Long ticketId;

    private Section newSection;

    private String newSeatNumber;
}
