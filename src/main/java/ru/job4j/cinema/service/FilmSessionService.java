package ru.job4j.cinema.service;

import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionService {

    Collection<FilmSession> findAll();

    Collection<FilmSession> findByFilmId(int id);

    Optional<FilmSession> findById(int id);
}
