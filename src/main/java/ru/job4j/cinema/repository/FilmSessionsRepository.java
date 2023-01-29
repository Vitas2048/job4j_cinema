package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;

public interface FilmSessionsRepository {

    Collection<FilmSession> findAll();

    Collection<FilmSession> findByFilmId(int id);
}
