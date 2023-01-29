package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.File;

import java.util.Collection;

public interface FileRepository {

    Collection<File> findAll();
}
