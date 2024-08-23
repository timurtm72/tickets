package org.example.ticketstest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TicketWrapper {
    @JsonProperty("tickets")
    private List<Ticket> tickets; // Список билетов

    // Конструктор по умолчанию
    public TicketWrapper() {
    }

    // Геттеры и сеттеры
    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}