package ru.job4j.cinema.repository;


import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oUserRepository implements UserRepository {

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        try (var connection = sql2o.open()) {
            try {
                var sql = """
                        INSERT INTO  users (full_name, email, password)
                        VALUES (:full_name, :email, :password)
                        """;
                var query = connection.createQuery(sql, true)
                        .addParameter("full_name", user.getFullName())
                        .addParameter("email", user.getEmail())
                        .addParameter("password", user.getPassword());
                int generatedId = query.executeUpdate().getKey(Integer.class);
                user.setId(generatedId);
                return Optional.of(user);
            } catch (Exception e) {
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("""
                    SELECT * FROM users where email = :email AND password = :password
                    """);
            query.addParameter("email", email)
                    .addParameter("password", password);
            var user = query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }

    public Collection<User> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users");
            return query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetch(User.class);
        }
    }

    public boolean deleteById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM users WHERE id = :id");
            query.addParameter("id", id);
            var affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }
}
