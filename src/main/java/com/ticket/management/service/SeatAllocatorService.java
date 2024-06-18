package com.ticket.management.service;

import com.ticket.management.dao.SeatRepository;
import com.ticket.management.entity.SeatEntity;
import com.ticket.management.enums.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class SeatAllocatorService implements CommandLineRunner {

    @Autowired
    private SeatRepository seatRepository;

    @Override
    public void run(String... args) throws Exception {
        createTenSeatsForEachSections();
    }

    private void createTenSeatsForEachSections() {
        Stream.of(Section.values()).forEach(section ->
                IntStream.rangeClosed(1, 10).forEach(index -> saveSeatEntity(section, index)));
    }

    private void saveSeatEntity(Section section, int index) {
        SeatEntity seat = new SeatEntity();
        seat.setSeatNumber(section.name() + index);
        seat.setSection(section);
        seat.setIsBooked(false);
        seatRepository.save(seat);
    }
}
