package org.example.ticketstest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    public double getAveragePrice(List<Integer> prices) {
        return prices.stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    public double getMedianPrice(List<Integer> prices) {
        int size = prices.size();
        if (size % 2 == 0) {
            return (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        } else {
           return  prices.get(size / 2);
        }
    }

    public Map<String, Long> getMinFlightTimeByCarrier(List<Ticket> filteredTickets){

        Map<String, Long> minFlightTimeByCarrierLocal = new HashMap<>();

        for (Ticket ticket : filteredTickets) {
            // Преобразование строковых полей в LocalDate и LocalTime
            LocalDate departureDate = LocalDate.parse(ticket.getDepartureDate(), Utils.DATE_FORMATTER);
            LocalTime departureTime = LocalTime.parse(ticket.getDepartureTime(), Utils.TIME_FORMATTER);
            LocalDate arrivalDate = LocalDate.parse(ticket.getArrivalDate(), Utils.DATE_FORMATTER);
            LocalTime arrivalTime = LocalTime.parse(ticket.getArrivalTime(), Utils.TIME_FORMATTER);

            // Создание LocalDateTime для вычисления времени полета
            LocalDateTime departureDateTime = LocalDateTime.of(departureDate, departureTime);
            LocalDateTime arrivalDateTime = LocalDateTime.of(arrivalDate, arrivalTime);

            // Рассчитываем время полета в минутах
            long flightTime = ChronoUnit.MINUTES.between(departureDateTime, arrivalDateTime);

            // Обновляем минимальное время полета для данного перевозчика
            minFlightTimeByCarrierLocal.merge(ticket.getCarrier(), flightTime, Math::min);
        }
        return minFlightTimeByCarrierLocal;
    }
}
