package com.ticket.management.dao;

import com.ticket.management.entity.TicketEntity;
import com.ticket.management.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class TicketRepositoryTest {

//    @Mock
//    private TicketRepository ticketRepository;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideTicketsForSave")
//    public void testSaveTicket(TicketEntity ticketEntity) {
//
//        //Arrange
//        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(ticketEntity);
//
//        //Act
//        TicketEntity savedTicket = ticketRepository.save(ticketEntity);
//
//        //Assert
//        assertNotNull(savedTicket.getId());
//        assertEquals(ticketEntity.getBoardingStation(), savedTicket.getBoardingStation());
//        assertEquals(ticketEntity.getDestinationStation(), savedTicket.getDestinationStation());
//        assertEquals(ticketEntity.getTicketPrice(), savedTicket.getTicketPrice());
//        assertEquals(ticketEntity.getTravelDate(), savedTicket.getTravelDate());
//    }
//
//    private static Stream<Arguments> provideTicketsForSave() {
//
//        TicketEntity ticketEntity = new TicketEntity(null, "London", "Paris", 5, LocalDate.now(), LocalDate.now(), null);
//        ticketEntity.setId(1L);
//
//        TicketEntity ticketEntity1 = new TicketEntity(null, "New York", "Los Angeles", 5, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), null);
//        ticketEntity1.setId(2L);
//
//        return Stream.of(
//                Arguments.of(ticketEntity),
//                Arguments.of(ticketEntity1)
//        );
//    }
//
//    @ParameterizedTest
//    @ValueSource(longs = {1L, 2L})
//    public void testFindTicketById(Long ticketId) {
//        TicketEntity ticketEntity = new TicketEntity(new UserEntity(), "London", "Paris", 5, LocalDate.now(), LocalDate.now(), null);
//        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticketEntity));
//
//        Optional<TicketEntity> optionalTicket = ticketRepository.findById(ticketId);
//
//        assertTrue(optionalTicket.isPresent());
//        assertEquals(ticketEntity.getBoardingStation(), optionalTicket.get().getBoardingStation());
//        assertEquals(ticketEntity.getDestinationStation(), optionalTicket.get().getDestinationStation());
//        assertEquals(ticketEntity.getTicketPrice(), optionalTicket.get().getTicketPrice());
//        assertEquals(ticketEntity.getTravelDate(), optionalTicket.get().getTravelDate());
//    }


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void testSaveTicket() {
        // Arrange
        UserEntity user = new UserEntity("Ash", "Ravi", "Ash.Ravi@gmail.com", "12345","London", null);
        UserEntity savedUser = entityManager.persistAndFlush(user);

        TicketEntity ticketEntity = new TicketEntity(savedUser,"London","Paris",5, LocalDate.now(),LocalDate.now(),null);

        // Act
        TicketEntity savedTicket = ticketRepository.save(ticketEntity);
        entityManager.flush(); // Flush changes to the database
        entityManager.clear(); // Clear EntityManager to ensure fresh data from the database

        // Assert
        assertNotNull(savedTicket.getId());
        assertEquals("London", savedTicket.getBoardingStation());
        assertEquals("Paris", savedTicket.getDestinationStation());
    }

    @Test
    public void testFindTicketById() {
        // Arrange
        UserEntity user = new UserEntity("Ash", "Ravi", "Ash.Ravi@gmail.com", "12345","London", null);
        UserEntity savedUser = entityManager.persistAndFlush(user);
        TicketEntity ticketEntity = new TicketEntity(savedUser,"London","Paris",5, LocalDate.now(),LocalDate.now(),null);
        TicketEntity savedTicket = entityManager.persistAndFlush(ticketEntity);

        // When
        Optional<TicketEntity> optionalTicketEntity = ticketRepository.findById(savedTicket.getId());

        // Then
        assertTrue(optionalTicketEntity.isPresent());
        assertEquals("London", optionalTicketEntity.get().getBoardingStation());
        assertEquals("Paris", optionalTicketEntity.get().getDestinationStation());
    }
}
