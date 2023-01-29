package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;

@Repository
public class Sql2oFilmSessionsRepository implements FilmSessionsRepository {

    private final Sql2o sql2o;

    public Sql2oFilmSessionsRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<FilmSession> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT * FROM film_sessions
                    """);
            return query.executeAndFetch(FilmSession.class);
        }
    }

    @Override
    public Collection<FilmSession> findByFilmId(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT * FROM film_sessions where film_id=:id
                    """)
                    .addParameter("filmId", id);
            return query.executeAndFetch(FilmSession.class);
        }
    }
}
