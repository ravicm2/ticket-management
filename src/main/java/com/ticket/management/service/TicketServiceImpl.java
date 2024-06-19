package com.ticket.management.service;

import com.ticket.management.dao.SeatRepository;
import com.ticket.management.dao.TicketRepository;
import com.ticket.management.dao.UserRepository;
import com.ticket.management.dto.SeatDTO;
import com.ticket.management.dto.TicketDTO;
import com.ticket.management.dto.TicketPurchaseRequestDTO;
import com.ticket.management.dto.UserDTO;
import com.ticket.management.entity.SeatEntity;
import com.ticket.management.entity.TicketEntity;
import com.ticket.management.entity.UserEntity;
import com.ticket.management.enums.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketServiceImpl {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private UserRepository userRepository;

    private static void createTicketEntity(TicketPurchaseRequestDTO requestDTO, UserEntity finalUserEntity, SeatEntity seatEntity, List<TicketEntity> ticketEntityList) {
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setBookingDate(LocalDate.now());
        ticketEntity.setBoardingStation(requestDTO.getFromLocation());
        ticketEntity.setDestinationStation(requestDTO.getToLocation());
        ticketEntity.setTravelDate(requestDTO.getTravelDate());
        ticketEntity.setUser(finalUserEntity);
        ticketEntity.setSeat(seatEntity);
        ticketEntityList.add(ticketEntity);
    }

    public List<TicketDTO> purchaseTicket(TicketPurchaseRequestDTO requestDTO) throws Exception {

        if (requestDTO.getQuantity() > 2) {
            throw new Exception("Max 2 tickets are permitted for a user");
        }

        long totalAvailableSeats = seatRepository.countBySectionAndIsBookedFalse(Section.valueOf(requestDTO.getSection()));
        if (totalAvailableSeats < (long) requestDTO.getQuantity())
            throw new Exception("Seats are not available for the requested Quantity. Available seats in the section are :" + totalAvailableSeats);

        List<SeatEntity> seatEntityList = seatRepository.findAvailableSeats(requestDTO.getSection(), PageRequest.of(0, requestDTO.getQuantity()));

        List<SeatEntity> updatedSeatEntityList = seatEntityList.stream().map(seatEntity -> {
            seatEntity.setIsBooked(true);
            return seatRepository.save(seatEntity);
        }).toList();

        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(requestDTO.getUser().getFirstName());
        userEntity.setPhoneNumber(requestDTO.getUser().getPhoneNumber());
        userEntity.setAddress(requestDTO.getUser().getAddress());
        userEntity.setEmail(requestDTO.getUser().getEmail());
        userEntity.setLastName(requestDTO.getUser().getLastName());

        userEntity = userRepository.save(userEntity);

        List<TicketEntity> ticketEntityList = new ArrayList<>();
        UserEntity finalUserEntity = userEntity;

        updatedSeatEntityList.forEach(seatEntity -> createTicketEntity(requestDTO, finalUserEntity, seatEntity, ticketEntityList));

//        IntStream.rangeClosed(1, requestDTO.getQuantity()).forEach(index -> createTicketEntity(requestDTO, finalUserEntity, updatedSeatEntity, ticketEntityList));

        return ticketEntityList.stream().map(this::createTicketAndReceipt).collect(Collectors.toList());
    }

    public TicketDTO getTicket(Long id) throws Exception {

        Optional<TicketEntity> optionalTicketEntity = ticketRepository.findById(id);

        return optionalTicketEntity.map(this::createReceipt).orElseThrow(() -> new Exception("Ticket not found"));

    }

    public void removeUserTicket(Long ticketId) throws Exception {

        TicketEntity ticketEntity = ticketRepository.findById(ticketId).orElseThrow(() -> new Exception("Ticket not found!"));

        long seatId = ticketEntity.getSeat().getId();
        long userId = ticketEntity.getUser().getId();

        SeatEntity seatEntity = seatRepository.findById(seatId).get();
        seatEntity.setIsBooked(false);
        seatRepository.save(seatEntity);
        ticketRepository.deleteById(ticketId);
        userRepository.deleteById(userId);
    }

    public TicketDTO modifySeat(Long ticketId, Section newSection, String newSeatNumber) throws Exception {

        TicketEntity ticketEntity = ticketRepository.findById(ticketId).orElseThrow(() -> new Exception("Ticket not found!"));

        // validate seat is already booked
        SeatEntity newSeatEntity = seatRepository.findBySectionAndSeatNumber(newSection, newSeatNumber).get();
        if (newSeatEntity.getIsBooked()) {
            throw new Exception("Selected seat is not available");
        }

        //load current ticket
        long seatId = ticketEntity.getSeat().getId();
        SeatEntity currentSeatEntity = seatRepository.findById(seatId).get();

        // change the status as not booked
        currentSeatEntity.setIsBooked(false);
        seatRepository.save(currentSeatEntity);

        //Create new seat
        newSeatEntity.setIsBooked(true);
        newSeatEntity = seatRepository.save(newSeatEntity);

        ticketEntity.setSeat(newSeatEntity);
        ticketEntity = ticketRepository.save(ticketEntity);

        return createReceipt(ticketEntity);
    }

    public List<SeatDTO> getSeatsBySection(String section) throws Exception {

        List<SeatEntity> seatEntityList = seatRepository.findBySection(Section.valueOf(section));

        if (seatEntityList.isEmpty())
            throw new Exception("No seats are available. Kindly enter the correct section");

        return seatEntityList.stream().map(this::createSeatDTO).collect(Collectors.toList());
    }

    private TicketDTO createTicketAndReceipt(TicketEntity ticketEntity) {
        ticketEntity = ticketRepository.save(ticketEntity);

        return createReceipt(ticketEntity);
    }

    private TicketDTO createReceipt(TicketEntity ticketEntity) {

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticketEntity.getId());
        ticketDTO.setPrice(ticketEntity.getTicketPrice());
        ticketDTO.setBookingDate(ticketEntity.getBookingDate());
        ticketDTO.setBoardingStation(ticketEntity.getBoardingStation());
        ticketDTO.setDestinationStation(ticketEntity.getDestinationStation());
        ticketDTO.setTravelDate(ticketEntity.getTravelDate());

        SeatDTO seatDTO = new SeatDTO();
        seatDTO.setId(ticketEntity.getSeat().getId());
        seatDTO.setIsBooked(ticketEntity.getSeat().getIsBooked());
        seatDTO.setSeatNumber(ticketEntity.getSeat().getSeatNumber());
        seatDTO.setSection(String.valueOf(ticketEntity.getSeat().getSection()));
        ticketDTO.setSeatDTO(seatDTO);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(ticketEntity.getUser().getId());
        userDTO.setEmail(ticketEntity.getUser().getEmail());
        userDTO.setFirstName(ticketEntity.getUser().getFirstName());
        userDTO.setLastName(ticketEntity.getUser().getLastName());
        userDTO.setPhoneNumber(ticketEntity.getUser().getPhoneNumber());
        ticketDTO.setUserDTO(userDTO);

        return ticketDTO;
    }

    private SeatDTO createSeatDTO(SeatEntity seatEntity) {

        SeatDTO seatDTO = new SeatDTO();
        seatDTO.setId(seatEntity.getId());
        seatDTO.setSeatNumber(seatEntity.getSeatNumber());
        seatDTO.setSection(String.valueOf(seatEntity.getSection()));
        seatDTO.setIsBooked(seatEntity.getIsBooked());
        return seatDTO;
    }
}
