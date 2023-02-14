package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {

    Collection<Ticket> findAll();

    Collection<Ticket> findByUserId(int id);

    Collection<Ticket> findByFilmSessionId(int id);

    boolean createByUser(int userId, int sessionId, int rowNumber, int placeNumber);
}
