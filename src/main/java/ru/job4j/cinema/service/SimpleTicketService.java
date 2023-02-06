package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    public SimpleTicketService(TicketRepository sql2oTicketRepository) {
        this.ticketRepository = sql2oTicketRepository;
    }

    @Override
    public Collection<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public Collection<Ticket> findByFilmSessionId(int id) {
        return ticketRepository.findByFilmSessionId(id);
    }

    @Override
    public Collection<Ticket> findByUserId(User user) {
        return ticketRepository.findByUserId(user);
    }

    @Override
    public boolean buyByUser(int userId, int sessionId, int rowNumber, int placeNumber) {
        return ticketRepository.createByUser(userId, sessionId, rowNumber, placeNumber);
    }
}
