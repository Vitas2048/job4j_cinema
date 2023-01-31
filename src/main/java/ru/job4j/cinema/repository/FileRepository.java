package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FileRepository {

    Collection<File> findAll();

    Optional<File> findById(int id);
}
