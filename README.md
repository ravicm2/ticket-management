# ticket-management

# Ticket Management System

## Overview

This project is a Ticket Management System designed to handle ticket purchases, seat modifications, and seat availability tracking. It provides RESTful APIs for various operations related to tickets and seats.

### Technologies and Libraries Used

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database (in-memory database for testing)
- Mockito (for unit testing)
- Spring MockMvc (for integration testing)

shell
Copy code

## APIs

### 1. Purchase Ticket

#### Endpoint

POST /api/ticket/purchase

#### Input JSON

```json
{
  "user": {
    "id": 1,
    "firstName": "Ash",
    "lastName": "Ravi",
    "email": "Ash.Ravi@gmail.com",
    "phoneNumber": "123-456-7890",
    "address": "London"
  },
  "fromLocation": "London",
  "toLocation": "Paris",
  "travelDate": "2024-08-20",
  "section": "A",
  "seatNumber": "1",
  "quantity": 2
}
```
##### Expected Output
- HTTP Status: 201 Created
- Response JSON: List of TicketDTO objects

### 2. Get Ticket
#### Endpoint
- GET /api/ticket/{ticketId}
##### Path Parameters
- ticketId: Long (ID of the ticket)
#### Expected Output
- HTTP Status: 200 OK
- Response JSON: TicketDTO object

### 3. Remove User Ticket.

#### Endpoint

- DELETE /api/ticket/remove/{ticketId}\

##### Path Parameters

- ticketId: Long (ID of the ticket to remove)

##### Expected Output
- HTTP Status: 204 No Content

### 4. Modify Seat
#### Endpoint
- PUT /api/ticket/modify-seat
##### Input JSON
```json
{
  "ticketId": 1,
  "newSection": "B",
  "newSeatNumber": "2"
}
```
#### Expected Output
- HTTP Status: 200 OK
- Response JSON: TicketDTO object

### 5. Get Seats by Section
#### Endpoint
- GET /api/ticket/seats/{section}
#### Path Parameters
- section: String (Section name, e.g., "A", "B")
#### Expected Output
- HTTP Status: 200 OK
- Response JSON: List of SeatDTO objects

## Running the Application
### Clone the repository:

````
git clone https://github.com/your-username/ticket-management.git
````

- Navigate to the project directory:
````
cd ticket-management
````

## Build the project using Maven:
````
mvn clean install
````
### Run the application:
````
mvn spring-boot:run
````
The application will start at http://localhost:8080.
