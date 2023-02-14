package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class Sql2oTicketRepositoryTest {
    private static Sql2oTicketRepository sql2oTicketRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oTicketRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var tickets = sql2oTicketRepository.findAll();
        for (var ticket : tickets) {
            sql2oTicketRepository.deleteById(ticket.getId());
        }
    }

    @Test
    public void when2SameTickets() {
        var buy1 = sql2oTicketRepository.createByUser(2, 1, 12, 12);
        var buy2 = sql2oTicketRepository.createByUser(2, 1, 12, 12);
        assertThat(buy2).isFalse();
    }

    @Test
    public void when2SameTicketsDifferentUsers() {
        var buy1 = sql2oTicketRepository.createByUser(2, 1, 12, 12);
        var buy2 = sql2oTicketRepository.createByUser(3, 1, 12, 12);
        assertThat(buy1).isTrue();
        assertThat(buy2).isFalse();
    }

    @Test
    public void whenCreated() {
        Ticket ticket = new Ticket(1, 1, 12, 12, 2);
        Ticket ticket1 = new Ticket(0, 1, 12, 13, 2);
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket);
        tickets.add(ticket1);
        sql2oTicketRepository.createByUser(2, 1, 12, 12);
        sql2oTicketRepository.createByUser(2, 1, 12, 13);
        var result = sql2oTicketRepository.findByUserId(2);
        assertThat(result.stream().map(Ticket::getPlaceNumber).collect(Collectors.toList()))
                .isEqualTo(tickets.stream().map(Ticket::getPlaceNumber).collect(Collectors.toList()));
        assertThat(result.stream().map(Ticket::getRowNumber).collect(Collectors.toList()))
                .isEqualTo(tickets.stream().map(Ticket::getRowNumber).collect(Collectors.toList()));
        assertThat(result.stream().map(Ticket::getSessionId).collect(Collectors.toList()))
                .isEqualTo(tickets.stream().map(Ticket::getSessionId).collect(Collectors.toList()));
    }
}