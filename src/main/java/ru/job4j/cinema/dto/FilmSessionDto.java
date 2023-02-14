package ru.job4j.cinema.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class FilmSessionDto {
    private int id;

    private String filmName;

    private String hallName;

    private LocalDateTime timeStart;

    private LocalDateTime timeEnd;

    public FilmSessionDto(int id, String filmName, String hallName, LocalDateTime timeStart, LocalDateTime timeEnd) {
        this.id = id;
        this.filmName = filmName;
        this.hallName = hallName;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilmSessionDto that = (FilmSessionDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
