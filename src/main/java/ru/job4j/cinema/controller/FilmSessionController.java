package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.*;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.HallService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sessions")
public class FilmSessionController {
    private final FilmSessionService filmSessionService;

    private final FilmService filmService;

    private final HallService hallService;

    private final TicketService ticketService;

    public FilmSessionController(FilmSessionService filmSessionService,
                                 FilmService filmService, HallService hallService, TicketService ticketService) {
        this.filmSessionService = filmSessionService;
        this.filmService = filmService;
        this.hallService = hallService;
        this.ticketService = ticketService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("filmSessions", filmSessionService.findAll().stream().map(p -> {
            return new FilmSessionDto(p.getId(), filmService.findById(p.getFilmId()).get().getName(),
                    hallService.findById(p.getHallsId()).get().getName(), p.getStartTime(), p.getEndTime());
        }).collect(Collectors.toList()));
        return "sessions/schedule";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id, @SessionAttribute User user, Model model) {
        var filmSessionOptional = filmSessionService.findById(id);
        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Данный сееанс не найден");
            return "errors/404";
        }
        var film = filmService.findById(filmSessionOptional.get().getFilmId());
        var hall = hallService.findById(filmSessionOptional.get().getHallsId());
        if (film.isEmpty()) {
            model.addAttribute("message", "Данные фильма не найдены");
            return "errors/404";
        }
        var rowCount = IntStream.rangeClosed(1, hall.get().getRowCount()).toArray();
        var placeCount = IntStream.rangeClosed(1, hall.get().getPlaceCount()).toArray();
        var ticket = new TicketDto();
        ticket.setSessionId(filmSessionOptional.get().getId());
        ticket.setUserId(user.getId());
        model.addAttribute("rowCounts", rowCount);
        model.addAttribute("placeCounts", placeCount);
        model.addAttribute("hall", hall.get());
        model.addAttribute("filmSession", filmSessionOptional.get());
        model.addAttribute("film", film.get());
        model.addAttribute("ticketDto", ticket);
        return "sessions/tickets";
    }

    @PostMapping("/buy")
    public String buy(@ModelAttribute TicketDto ticketDto,
                      Model model) {
        try {
            var isNew = ticketService.buyByUser(ticketDto.getUserId(), ticketDto.getSessionId(),
                    ticketDto.getRowNumber(), ticketDto.getPlaceNumber());
            if (isNew) {
                model.addAttribute("message", "Билет - ряд: " + ticketDto.getRowNumber()
                        + " место: " + ticketDto.getPlaceNumber() + " - приобретен");
                return "sessions/accept";
            }
            model.addAttribute("message", "Билет - ряд: " + ticketDto.getRowNumber()
                    + " место: " + ticketDto.getPlaceNumber() + " - уже забронирован или куплен");
            return "errors/409";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "errors/404";
        }
    }
}
