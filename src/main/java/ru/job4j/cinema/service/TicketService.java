package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

public interface TicketService {

    void generate();

    Collection<Ticket> findAll();

    Optional<Collection<Ticket>> findByUserId(User user);

    Optional<Ticket> setUserId(User user, Ticket ticket);

}
