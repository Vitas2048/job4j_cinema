package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.*;
import ru.job4j.cinema.service.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmSessionControllerTest {

    private FilmSessionService filmSessionService;

    private FilmService filmService;

    private HallService hallService;

    private TicketService ticketService;

    private GenreService genreService;

    private FilmSessionController filmSessionController;

    private FilmController filmController;

    @BeforeEach
    public void intiServices() {
        filmSessionService = mock(FilmSessionService.class);
        filmService = mock(FilmService.class);
        hallService = mock(HallService.class);
        ticketService = mock(TicketService.class);
        genreService = mock(GenreService.class);

        filmSessionController = new FilmSessionController(filmSessionService,
                filmService, hallService, ticketService);
        filmController = new FilmController(filmService, genreService);
    }

    @Test
    public void whenRequestAllFilmSessionsThenGetPageWithSessions() {
        var filmSession = new FilmSession(1, 1, 1, LocalDateTime.now(), LocalDateTime.now().plusHours(3));
        var filmSession1 = new FilmSession(2, 2, 2, LocalDateTime.now(), LocalDateTime.now().plusHours(3));
        var sessions = List.of(filmSession, filmSession1);

        Film film = new Film(1, "1", "1", 23, 1, 5, 123, 2);
        Film film1 = new Film(2, "1", "1", 23, 1, 5, 123, 2);
        var films = List.of(film, film1);

        Hall hall = new Hall(1, " 1", 2, 3, "d");
        Hall hall1 = new Hall(2, "2 ", 3, 4, "d");
        var halls = List.of(hall, hall1);

        when(filmSessionService.findAll()).thenReturn(sessions);
        when(hallService.findById(1)).thenReturn(Optional.of(hall));
        when(hallService.findById(2)).thenReturn(Optional.of(hall1));
        when(filmService.findById(1)).thenReturn(Optional.of(film));
        when(filmService.findById(2)).thenReturn(Optional.of(film1));

        var expectedSessions = filmSessionService.findAll().stream().map(p -> {
            return new FilmSessionDto(p.getId(), filmService.findById(p.getFilmId()).get().getName(),
                    hallService.findById(p.getHallsId()).get().getName(), p.getStartTime(), p.getEndTime());
        }).toList();

        var model = new ConcurrentModel();
        var view = filmSessionController.getAll(model);
        var actualSessions = model.getAttribute("filmSessions");

        assertThat(view).isEqualTo("sessions/schedule");
        assertThat(actualSessions).isEqualTo(expectedSessions);
    }

    @Test
    public void whenRequestAllFilmsPageThenGetFilmsPage() {
        Film film = new Film(1, "1", "1", 23, 1, 5, 123, 2);
        Film film1 = new Film(2, "1", "1", 23, 1, 5, 123, 2);
        var films = List.of(film, film1);
        Genre genre1 = new Genre(1, "1");
        Genre genre = new Genre(2, "2");

        when(filmService.findAll()).thenReturn(films);
        when(genreService.findById(1)).thenReturn(Optional.of(genre1));
        when(genreService.findById(2)).thenReturn(Optional.of(genre));

        var expected = films.stream().map(p -> {
            return new FilmDto(p.getId(), p.getName(), p.getDescription(), p.getYear(),
                    genreService.findById(p.getGenreId()).get().getName(), p.getMinimalAge(),
                    p.getDurationInMinutes(), p.getFileId());
        }).toList();

        var model = new ConcurrentModel();
        var view = filmController.getAll(model);
        var actual = model.getAttribute("films");

        assertThat(view).isEqualTo("films/films");
        assertThat(expected).isEqualTo(actual);

    }

    @Test
    public void whenBuyThenAcceptedPage() {
        User user = new User(1, "1", "1", "1");
        TicketDto ticketDto = new TicketDto(1, 1, 1, 1);
        when(ticketService.buyByUser(user.getId(), ticketDto.getSessionId(),
                ticketDto.getRowNumber(), ticketDto.getPlaceNumber())).thenReturn(true);
        var result1 = "Билет - ряд: " + ticketDto.getRowNumber()
                + " место: " + ticketDto.getPlaceNumber() + " - приобретен";

        var model = new ConcurrentModel();
        var view = filmSessionController.buy(ticketDto, model);
        var actual = model.getAttribute("message");

        assertThat(view).isEqualTo("sessions/accept");
        assertThat(actual).isEqualTo(result1);
    }

    @Test
    public void whenBuyOldThenFailedPage() {
        User user = new User(1, "1", "1", "1");
        TicketDto ticketDto = new TicketDto(1, 1, 1, 1);
        Ticket ticket = new Ticket(1, 1, 1, 1, 1);
        var result = List.of(ticket);
        when(ticketService.findByUserId(1)).thenReturn(result);
        when(ticketService.buyByUser(user.getId(), ticketDto.getSessionId(),
                ticketDto.getRowNumber(), ticketDto.getPlaceNumber())).thenReturn(false);
        var result1 = "Билет - ряд: " + ticketDto.getRowNumber()
                + " место: " + ticketDto.getPlaceNumber() + " - уже забронирован или куплен";

        var model = new ConcurrentModel();
        var view = filmSessionController.buy(ticketDto, model);
        var actual = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/409");
        assertThat(actual).isEqualTo(result1);
    }

    @Test
    public void whenGetSessionByIdThenTicketsPage() {
        User user = new User(1, "1", "1", "1");
        Film film = new Film(1, "1", "1", 23, 1, 5, 123, 2);
        Hall hall = new Hall(1, " 1", 2, 3, "d");
        var expectedRows = IntStream.rangeClosed(1, 2).toArray();
        var expectedPlaces = IntStream.rangeClosed(1, 3).toArray();
        FilmSession filmSession = new FilmSession(1, 1, 1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(filmSessionService.findById(1)).thenReturn(Optional.of(filmSession));
        when(hallService.findById(1)).thenReturn(Optional.of(hall));
        when(filmService.findById(1)).thenReturn(Optional.of(film));

        var model = new ConcurrentModel();
        var view = filmSessionController.getById(1, user, model);
        var actualRows = model.getAttribute("rowCounts");
        var actualPlaces = model.getAttribute("placeCounts");
        var actualHall = model.getAttribute("hall");
        var actualFilmSession = model.getAttribute("filmSession");
        var actualFilm = model.getAttribute("film");

        assertThat(view).isEqualTo("sessions/tickets");
        assertThat(actualFilmSession).isEqualTo(filmSession);
        assertThat(actualHall).isEqualTo(hall);
        assertThat(actualFilm).isEqualTo(film);
        assertThat(actualPlaces).isEqualTo(expectedPlaces);
        assertThat(actualRows).isEqualTo(expectedRows);
    }
}
