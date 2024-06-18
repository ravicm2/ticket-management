package com.ticket.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket.management.dto.ModifySeatRequestDTO;
import com.ticket.management.dto.TicketPurchaseRequestDTO;
import com.ticket.management.dto.UserDTO;
import com.ticket.management.enums.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TicketManagementApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	@Sql(scripts = {"/test-schema.sql", "/test-data.sql"})
	void setUp() {
	}

	@ParameterizedTest
	@MethodSource("ticketPurchaseRequestProvider")
	public void testPurchaseTicket(TicketPurchaseRequestDTO requestDTO, ResultMatcher expectedStatus) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/ticket/purchase")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(requestDTO)))
				.andExpect(expectedStatus);
	}

	private static Stream<Object[]> ticketPurchaseRequestProvider() {
		return Stream.of(
				new Object[]{createTicketPurchaseRequestDTO(1L, "New York", "Los Angeles", LocalDate.now().plusDays(1)), status().isCreated()},
				new Object[]{createTicketPurchaseRequestDTO(1L, "New York", "Los Angeles", LocalDate.now().plusDays(1)), status().isBadRequest()},
				new Object[]{createTicketPurchaseRequestDTO(99L, "New York", "Los Angeles", LocalDate.now().plusDays(1)), status().isBadRequest()}
		);
	}

	private static TicketPurchaseRequestDTO createTicketPurchaseRequestDTO(Long userId, String boardingStation, String destinationStation, LocalDate travelDate) {
		TicketPurchaseRequestDTO requestDTO = new TicketPurchaseRequestDTO();
		UserDTO userDTO = new UserDTO();
		userDTO.setId(userId);
		userDTO.setEmail("");
		userDTO.setLastName("");
		userDTO.setFirstName("");
		userDTO.setPhoneNumber("");
		requestDTO.setUser(new UserDTO());
		requestDTO.setFromLocation(boardingStation);
		requestDTO.setToLocation(destinationStation);
		requestDTO.setTravelDate(travelDate);
		return requestDTO;
	}

	@ParameterizedTest
	@ValueSource(longs = {1L})
	public void testGetTicket(Long ticketId) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/ticket/{ticketId}", ticketId)
						.contentType("application/json"))
				.andExpect(status().isOk());
	}

	@ParameterizedTest
	@ValueSource(longs = {99L})
	public void testGetTicketNotFound(Long ticketId) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/ticket/{ticketId}", ticketId)
						.contentType("application/json"))
				.andExpect(status().isNotFound());
	}

	@ParameterizedTest
	@ValueSource(longs = {1L})
	public void testRemoveUserTicket(Long ticketId) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/ticket/remove/{ticketId}", ticketId)
						.contentType("application/json"))
				.andExpect(status().isNoContent());
	}

	@ParameterizedTest
	@ValueSource(longs = {99L})
	public void testRemoveUserTicketNotFound(Long ticketId) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/ticket/remove/{ticketId}", ticketId)
						.contentType("application/json"))
				.andExpect(status().isNotFound());
	}

	@ParameterizedTest
	@MethodSource("modifySeatRequestProvider")
	public void testModifySeat(ModifySeatRequestDTO requestDTO, ResultMatcher expectedStatus) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/api/ticket/modify-seat")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(requestDTO)))
				.andExpect(expectedStatus);
	}

	private static Stream<Object[]> modifySeatRequestProvider() {
		return Stream.of(
				new Object[]{createModifySeatRequestDTO(1L, Section.B, "2"), status().isOk()},
				new Object[]{createModifySeatRequestDTO(1L, Section.B, "1"), status().isBadRequest()},
				new Object[]{createModifySeatRequestDTO(99L, Section.B, "2"), status().isNotFound()}
		);
	}

	private static ModifySeatRequestDTO createModifySeatRequestDTO(Long ticketId, Section newSection, String newSeatNumber) {
		ModifySeatRequestDTO requestDTO = new ModifySeatRequestDTO();
		requestDTO.setTicketId(ticketId);
		requestDTO.setNewSection(newSection);
		requestDTO.setNewSeatNumber(newSeatNumber);
		return requestDTO;
	}

	@ParameterizedTest
	@ValueSource(strings = {"A", "B"})
	public void testGetSeatsBySection(String section) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/ticket/seats/{section}", section)
						.contentType("application/json"))
				.andExpect(status().isOk());
	}

	@ParameterizedTest
	@ValueSource(strings = {"C", "D"})
	public void testGetSeatsBySectionNotFound(String section) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/ticket/seats/{section}", section)
						.contentType("application/json"))
				.andExpect(status().isBadRequest());
	}

}
