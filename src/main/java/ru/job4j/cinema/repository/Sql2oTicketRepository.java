package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.*;
import java.util.stream.IntStream;

@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private final Sql2o sql2o;

    private Map<FilmSession, List<Ticket>> tickets = new HashMap<>();


    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void generateAll() {
        try (var connection = sql2o.open()) {
            var query1 = connection.createQuery("SELECT * FROM film_sessions");
            var sessions = query1.executeAndFetch(FilmSession.class);
            sessions.forEach(s -> {
                var rowQuery = connection.createQuery("""
                        SELECT row_count FROM halls WHERE id = :row_id
                        """).addParameter("row_id", s.getHallsId());
                var rowCount = rowQuery.executeAndFetchFirst(Integer.class);
                var placeQuery = connection.createQuery("""
                        SELECT place_count FROM halls WHERE id = :place_id
                        """).addParameter("place_id", s.getHallsId());
                var placeCount = placeQuery.executeAndFetchFirst(Integer.class);

                IntStream.of(rowCount).forEach(f -> {
                    IntStream.of(placeCount).forEach(p -> {
                        connection.createQuery("""
                        INSERT INTO tickets(session_id, row_number, place_number, user_id)
                        VALUES (:session_id, :row_number, place_number, user_id)
                        """).addParameter("session_id", s.getId())
                                .addParameter("row_number", f)
                                .addParameter("place_count", p)
                                .addParameter("user_id", 0);
                    });
                });

            });

        }
    }

    @Override
    public Collection<Ticket> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT * FROM tickets
                    """);
            return query.executeAndFetch(Ticket.class);
        }
    }

    @Override
    public Optional<List<Ticket>> findByUserId(User user) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT FROM tickets WHERE user_id = :user_id
                    """).addParameter("user_id", user.getId());
            return Optional.ofNullable(query.executeAndFetch(Ticket.class));
        }
    }

    @Override
    public Optional<Ticket> setUserId(User user, Ticket ticket) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    UPDATE tickets SET user_id = :user_id where id = :ticket_id
                    """).addParameter("user_id", user.getId())
                    .addParameter("ticket_id", ticket.getId());
            ticket.setUserId(user.getId());
            return Optional.of(ticket);
        }
    }
}
