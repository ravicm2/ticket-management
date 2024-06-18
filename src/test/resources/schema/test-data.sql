-- Insert test data for ticket_user table
INSERT INTO ticket_user (first_name, last_name, email, phone_number, address) VALUES
('John', 'Doe', 'john.doe@example.com', '1234567890', '123 Main St'),
('Jane', 'Smith', 'jane.smith@example.com', '0987654321', '456 Elm St');

-- Insert test data for ticket table
INSERT INTO ticket (user_id, boarding_station, destination_station, travel_date, booking_date, seat_id) VALUES
(1, 'New York', 'Los Angeles', '2024-06-20', '2024-06-01', 1),
(2, 'Chicago', 'Miami', '2024-07-15', '2024-07-01', 3);

-- Insert test data for seat table
INSERT INTO seat (section, seat_number, is_booked, ticket_id) VALUES
('A', 'A1', TRUE, 1),
('A', 'A2', FALSE, NULL),
('B', 'B1', TRUE, 2),
('B', 'B2', FALSE, NULL);