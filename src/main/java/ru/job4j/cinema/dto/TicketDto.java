package ru.job4j.cinema.dto;

public class TicketDto {

    private int rowNumber;

    private int placeNumber;

    public TicketDto(int rowNumber, int placeNumber) {
        this.placeNumber = placeNumber;
        this.rowNumber = rowNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(int placeNumber) {
        this.placeNumber = placeNumber;
    }
}
