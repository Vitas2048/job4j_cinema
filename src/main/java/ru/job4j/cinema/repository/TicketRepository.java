package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TicketRepository {

    void generateAll();

    Collection<Ticket> findAll();

    Optional<List<Ticket>> findByUserId(User user);

    Optional<Ticket> setUserId(User user, Ticket ticket);
}
