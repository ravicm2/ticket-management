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
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

//    public TicketServiceImplTest() {
//        MockitoAnnotations.openMocks(this);
//    }

    @ParameterizedTest
    @MethodSource("providePurchaseTicket")
    public void testPurchaseTicket(TicketPurchaseRequestDTO requestDTO, boolean seatAvailable, boolean seatExists, boolean expectedResult, String expectedMessage) {
        // Mocking behavior
        when(seatRepository.countBySectionAndIsBookedFalse(any(Section.class))).thenReturn(seatAvailable ? 10L : 0L);
        when(seatRepository.findBySectionAndSeatNumber(any(Section.class), anyString())).thenReturn(seatExists ? Optional.of(new SeatEntity()) : Optional.empty());
        lenient().when(seatRepository.save(any(SeatEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(ticketRepository.save(any(TicketEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        try {
            // Execution
            List<TicketDTO> result = ticketService.purchaseTicket(requestDTO);

            // Verification
            assertTrue(expectedResult);
            assertNotNull(result);
        } catch (Exception e) {
            // Verification
            assertFalse(expectedResult);
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    private static Stream<Arguments> providePurchaseTicket() {
        TicketPurchaseRequestDTO validRequest = new TicketPurchaseRequestDTO();
        validRequest.setQuantity(2);
        validRequest.setSection("A");
        validRequest.setSeatNumber("1");
        validRequest.setFromLocation("London");
        validRequest.setToLocation("Paris");
        validRequest.setTravelDate(LocalDate.now().plusDays(1));
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");
        validRequest.setUser(userDTO);

        TicketPurchaseRequestDTO invalidQuantityRequest = new TicketPurchaseRequestDTO();
        invalidQuantityRequest.setQuantity(7);
        invalidQuantityRequest.setSection("A");
        invalidQuantityRequest.setSeatNumber("1");
        invalidQuantityRequest.setFromLocation("London");
        invalidQuantityRequest.setToLocation("Paris");
        invalidQuantityRequest.setTravelDate(LocalDate.now().plusDays(1));
        invalidQuantityRequest.setUser(userDTO);

        TicketPurchaseRequestDTO noSeatAvailableRequest = new TicketPurchaseRequestDTO();
        noSeatAvailableRequest.setQuantity(2);
        noSeatAvailableRequest.setSection("A");
        noSeatAvailableRequest.setSeatNumber("1");
        noSeatAvailableRequest.setFromLocation("London");
        noSeatAvailableRequest.setToLocation("Paris");
        noSeatAvailableRequest.setTravelDate(LocalDate.now().plusDays(1));
        noSeatAvailableRequest.setUser(userDTO);

        return Stream.of(
                Arguments.of(validRequest, true, true, true, ""),
                Arguments.of(invalidQuantityRequest, true, true, false, "Max 6 tickets are permitted for a user"),
                Arguments.of(noSeatAvailableRequest, false, true, false, "Seats are not available for the requested Quantity. Available seats in the section are :0")
        );
    }

    @ParameterizedTest
    @MethodSource("provideGetTicket")
    public void testGetTicket(Long ticketId, TicketEntity ticketEntity, boolean expectedResult, String expectedMessage) {
        // Mocking behavior
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ticketEntity));

        try {
            // Execution
            TicketDTO result = ticketService.getTicket(ticketId);

            // Verification
            assertTrue(expectedResult);
            assertNotNull(result);
        } catch (Exception e) {
            // Verification
            assertFalse(expectedResult);
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    private static Stream<Arguments> provideGetTicket() {
        TicketEntity validTicket = new TicketEntity();
        validTicket.setId(1L);
        validTicket.setSeat(new SeatEntity());
        validTicket.setUser(new UserEntity());

        return Stream.of(
                Arguments.of(1L, validTicket, true, ""),
                Arguments.of(2L, null, false, "Ticket not found")
        );
    }

    @ParameterizedTest
    @MethodSource("provideRemoveUserTicket")
    public void testRemoveUserTicket(Long ticketId, TicketEntity ticketEntity, boolean seatFound, boolean expectedResult, String expectedMessage) {
        // Mocking behavior
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ticketEntity));
        lenient().when(seatRepository.findById(anyLong())).thenReturn(seatFound ? Optional.of(new SeatEntity()) : Optional.empty());
        lenient().doNothing().when(ticketRepository).deleteById(anyLong());
        lenient().doNothing().when(userRepository).deleteById(anyLong());
        lenient().when(seatRepository.save(any(SeatEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        try {
            // Execution
            ticketService.removeUserTicket(ticketId);

            // Verification
            assertTrue(expectedResult);
        } catch (Exception e) {
            // Verification
            assertFalse(expectedResult);
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    private static Stream<Arguments> provideRemoveUserTicket() {
        TicketEntity validTicket = new TicketEntity();
        validTicket.setId(1L);
        SeatEntity seatEntity = new SeatEntity();
        seatEntity.setId(1L);
        validTicket.setSeat(seatEntity);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        validTicket.setUser(userEntity);

        return Stream.of(
                Arguments.of(1L, validTicket, true, true, ""),
                Arguments.of(2L, null, true, false, "Ticket not found!"),
                Arguments.of(1L, validTicket, false, false, "Ticket not found!")
        );
    }

    @ParameterizedTest
    @MethodSource("provideModifySeat")
    public void testModifySeat(Long ticketId, Section newSection, String newSeatNumber, TicketEntity ticketEntity, SeatEntity newSeatEntity, boolean seatFound, boolean expectedResult, String expectedMessage) {
        // Mocking behavior
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ticketEntity));
        when(seatRepository.findBySectionAndSeatNumber(any(Section.class), anyString())).thenReturn(Optional.ofNullable(newSeatEntity));
        lenient().when(seatRepository.save(any(SeatEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(ticketRepository.save(any(TicketEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        try {
            // Execution
            TicketDTO result = ticketService.modifySeat(ticketId, newSection, newSeatNumber);

            // Verification
            assertTrue(expectedResult);
            assertNotNull(result);
        } catch (Exception e) {
            // Verification
            assertFalse(expectedResult);
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    private static Stream<Arguments> provideModifySeat() {
        TicketEntity validTicket = new TicketEntity();
        validTicket.setId(1L);
        SeatEntity currentSeat = new SeatEntity();
        currentSeat.setId(1L);
        validTicket.setSeat(currentSeat);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        validTicket.setUser(userEntity);

        SeatEntity availableSeat = new SeatEntity();
        availableSeat.setIsBooked(false);

        SeatEntity bookedSeat = new SeatEntity();
        bookedSeat.setIsBooked(true);

        return Stream.of(
                Arguments.of(1L, Section.A, "2", validTicket, availableSeat, true, true, ""),
                Arguments.of(2L, Section.A, "2", null, availableSeat, true, false, "Ticket not found!"),
                Arguments.of(1L, Section.A, "2", validTicket, bookedSeat, true, false, "Selected seat is not available")
        );
    }
}
