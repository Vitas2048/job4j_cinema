package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oFileRepository implements FileRepository {

    private final Sql2o sql2o;

    public Sql2oFileRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    @Override
    public Collection<File> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT * FROM files
                    """);
            return query.executeAndFetch(File.class);
        }
    }

    public Optional<File> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT * FROM files WHERE id = :id
                    """)
                    .addParameter("id", id);
            return Optional.ofNullable(query.executeAndFetchFirst(File.class));
        }
    }
}
