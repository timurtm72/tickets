package org.example.ticketstest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            return prices.get(size / 2);
        }
    }

    public Map<String, Long> getMinFlightTimeByCarrier(List<Ticket> filteredTickets) {

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

    public Path getResourceFolderPath(String fileName) {
        try {
            // Получаем путь к корню папки resources
            return Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String readFilePath(String fileName, Utils utils) {
        Path filePath = utils.getResourceFolderPath("tickets.json");
        System.out.println(filePath);
        File file = new File(filePath.toString());
        return filePath.toString();
    }

    public TicketWrapper readFile(File file) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Десериализация JSON в объект-обертку
        TicketWrapper wrapper = null;
        try {
            wrapper = mapper.readValue(file, TicketWrapper.class);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        return wrapper;
    }
    public List<Ticket> filterTicketsAndPrint(List<Ticket> tickets) {
        System.out.println("=========================== Список билетов =================================");

        // Используем Stream API для вывода билетов с индексом
        IntStream.range(0, tickets.size())
                .forEach(idx -> System.out.println((idx + 1) + " => " + tickets.get(idx)));

        System.out.println("=========================== Список билетов Владивосток -> Тель-Авив ========");

        // Фильтруем билеты по направлениям "Владивосток" -> "Тель-Авив"
        List<Ticket> filteredTickets = tickets.stream()
                .filter(ticket -> "VVO".equals(ticket.getOrigin()) && "TLV".equals(ticket.getDestination()))
                .collect(Collectors.toList());

        IntStream.range(0, filteredTickets.size())
                .forEach(idx -> System.out.println((idx + 1) + " => " + filteredTickets.get(idx)));
        return filteredTickets;
    }
    public void printMinFlightTimeByCarrier(Map<String,Long> map) {
        // Вывод минимального времени полета для каждого перевозчика
        System.out.println("Минимальное время полета между Владивостоком и Тель-Авивом для каждого авиаперевозчика:");

        map.forEach((carrier, time) -> {
            // Переводим время из минут в часы и минуты
            long hours = time / 60; // Часы
            long minutes = time % 60; // Оставшиеся минуты

            // Форматируем строку для вывода
            System.out.println(carrier + ": " + hours + " ч " + minutes + " мин");
        });
    }

    public void printAverageAndMedian(Utils utils,List<Integer> prices){
        double averagePrice = utils.getAveragePrice(prices);
        // Рассчитываем медианную цену
        double medianPrice = utils.getMedianPrice(prices);

        // Вывод разницы между средней и медианной ценой
        System.out.println("\nРазница между средней ценой и медианной ценой для полетов между Владивостоком и Тель-Авивом:");
        System.out.println("Средняя цена: " + averagePrice);
        System.out.println("Медианная цена: " + medianPrice);
        System.out.println("Разница: " + (averagePrice - medianPrice));
    }
    public List<Integer> getPrices(List<Ticket> tickets) {
        return tickets.stream().map(Ticket::getPrice).sorted().collect(Collectors.toList());
    }
}
