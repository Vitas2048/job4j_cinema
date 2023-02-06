package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.*;
import java.util.stream.IntStream;

@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }


    @Override
    public Collection<Ticket> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT * FROM tickets
                    """);
            return query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetch(Ticket.class);
        }
    }

    @Override
    public Collection<Ticket> findByUserId(User user) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT FROM tickets WHERE user_id = :user_id
                    """).addParameter("user_id", user.getId());
            return query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetch(Ticket.class);
        }
    }

    @Override
    public Collection<Ticket> findByFilmSessionId(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT * FROM tickets WHERE session_id = :session_id
                    """).addParameter("session_id", id);
            return query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetch(Ticket.class);
        }
    }

    @Override
    public boolean createByUser(int userId, int sessionId, int rowNumber, int placeNumber) {
            try (var connection = sql2o.open()) {
                var query = connection.createQuery("""
                    INSERT INTO tickets(session_id, row_number, place_number, user_id) 
                    VALUES(:session_id, :row_number, :place_number, :user_id)
                    """).addParameter("session_id", sessionId)
                        .addParameter("row_number", rowNumber)
                        .addParameter("place_number", placeNumber)
                        .addParameter("user_id", userId);
                var isOnly = findByRowPlaceSession(sessionId, rowNumber, placeNumber);
                if (isOnly) {
                    var result = query.executeUpdate().getResult();
                    return result > 0;
                }
                return false;
            }
    }

    private boolean findByRowPlaceSession(int sessionId, int rowNumber, int placeNumber) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT * FROM tickets WHERE session_id=:session_id AND
                    place_number=:place_number AND row_number=:row_number
                    """).addParameter("session_id", sessionId)
                    .addParameter("row_number", rowNumber)
                    .addParameter("place_number", placeNumber);
            return Optional.ofNullable(query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetchFirst(Ticket.class)).isEmpty();
        }
    }
}
