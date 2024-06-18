package com.ticket.management.configuration;

import com.ticket.management.dao.SeatRepository;
import com.ticket.management.entity.SeatEntity;
import com.ticket.management.enums.Section;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Configuration
public class SeatEntityLoaderTest {

    @Bean
    public CommandLineRunner loadData(SeatRepository seatRepository) {
        return args -> Stream.of(Section.values()).forEach(section ->
                IntStream.rangeClosed(1, 5).forEach(index -> {
                    SeatEntity seat = new SeatEntity();
                    seat.setSeatNumber(section.name() + index);
                    seat.setSection(section);
                    seat.setIsBooked(false);
                    seatRepository.save(seat);
                }));
    }
}
