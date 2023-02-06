package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

public interface TicketService {

    Collection<Ticket> findAll();

    Collection<Ticket> findByFilmSessionId(int id);

    Collection<Ticket> findByUserId(User user);

    boolean buyByUser(int userId, int sessionId, int rowNumber, int placeNumber);

}
