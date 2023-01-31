package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FileService;
import ru.job4j.cinema.service.FilmService;
import ru.job4j.cinema.service.GenreService;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/films")
public final class FilmController {

    private final FilmService filmService;

    private final GenreService genreService;


    public FilmController(FilmService filmService, GenreService genreService) {
        this.filmService = filmService;
        this.genreService = genreService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("films", filmService.findAll().stream().map(p -> {
            return new FilmDto(p.getId(), p.getName(), p.getDescription(), p.getYear(),
                    genreService.findById(p.getGenreId()).get().getName(), p.getMinimalAge(),
                    p.getDurationInMinutes(), p.getFileId());
        }).collect(Collectors.toList()));
        return "films/films";
    }
}
