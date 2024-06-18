package com.ticket.management.controller;

import com.ticket.management.dto.ModifySeatRequestDTO;
import com.ticket.management.dto.SeatDTO;
import com.ticket.management.dto.TicketDTO;
import com.ticket.management.dto.TicketPurchaseRequestDTO;
import com.ticket.management.service.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    private TicketServiceImpl ticketServiceImpl;

    /*@Autowired
    private ValidationService validationService;*/

    @PostMapping(value = "/purchase", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> purchaseTicket(@RequestBody TicketPurchaseRequestDTO requestDTO) {

        // validate requestDTO to ignore XSS
        // validationService.validate(requestDTO);

        List<TicketDTO> ticketDTOList;
        try {
            ticketDTOList = ticketServiceImpl.purchaseTicket(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(ticketDTOList);
        } catch (Exception exception) {
            // handle exception with ExceptionHandler and ControllerAdvice
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{ticketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTicket(@PathVariable Long ticketId) {

        // validate requestDTO to ignore XSS
        // validationService.validate(id);

        try {
            TicketDTO ticketDTO = ticketServiceImpl.getTicket(ticketId);
            return ResponseEntity.ok(ticketDTO);
        } catch (Exception exception) {
            // handle exception with ExceptionHandler and ControllerAdvice
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/remove/{ticketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeUser(@PathVariable Long ticketId) {

        // validate requestDTO to ignore XSS
        // validationService.validate(id);

        try {
            ticketServiceImpl.removeUserTicket(ticketId);
            return ResponseEntity.noContent().build();
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/modify-seat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> modifySeat(@RequestBody ModifySeatRequestDTO modifySeatRequestDTO) throws Exception {

        // validate modifySeatRequestDTO to ignore XSS
        // validationService.validate(id);

        try {
            TicketDTO ticketDTO = ticketServiceImpl.modifySeat(modifySeatRequestDTO.getTicketId(), modifySeatRequestDTO.getNewSection(), modifySeatRequestDTO.getNewSeatNumber());
            return ResponseEntity.status(HttpStatus.CREATED).body(ticketDTO);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/seats/{section}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSeatsBySection(@PathVariable String section) {

        // validate modifySeatRequestDTO to ignore XSS
        // validationService.validate(id);
        try {
            List<SeatDTO> seatDTOs = ticketServiceImpl.getSeatsBySection(section);
            return ResponseEntity.ok(seatDTOs);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
