package org.example.ticketstest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketAnalyzer {
    public static void main(String[] args) {
        // Путь к файлу
        String workingDir = System.getProperty("user.dir");
        String filePath = workingDir + File.separator + "tickets.json";
        File file = new File(filePath);

        // Проверка на существование файла
        if (file.exists()) {
            // Создаем ObjectMapper для чтения JSON
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Десериализация JSON в объект-обертку
            TicketWrapper wrapper = null;
            try {
                wrapper = mapper.readValue(file, TicketWrapper.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("=========================== Список билетов =================================");

            // Получение списка билетов
            List<Ticket> tickets = wrapper.getTickets();
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

            Utils utils = new Utils();
            // Минимальное время полета для каждого перевозчика
            Map<String, Long> minFlightTimeByCarrier = new HashMap<>();
            minFlightTimeByCarrier = utils.getMinFlightTimeByCarrier(filteredTickets);

            // Вывод минимального времени полета для каждого перевозчика
            System.out.println("Минимальное время полета между Владивостоком и Тель-Авивом для каждого авиаперевозчика:");

            minFlightTimeByCarrier.forEach((carrier, time) -> {
                // Переводим время из минут в часы и минуты
                long hours = time / 60; // Часы
                long minutes = time % 60; // Оставшиеся минуты

                // Форматируем строку для вывода
                System.out.println(carrier + ": " + hours + " ч " + minutes + " мин");
            });


            // Получаем список цен на билеты и сортируем его
            List<Integer> prices = filteredTickets.stream().map(Ticket::getPrice).sorted().collect(Collectors.toList());

            // Рассчитываем среднюю цену
            double averagePrice = utils.getAveragePrice(prices);

            // Рассчитываем медианную цену
            double medianPrice = utils.getMedianPrice(prices);

            // Вывод разницы между средней и медианной ценой
            System.out.println("\nРазница между средней ценой и медианной ценой для полетов между Владивостоком и Тель-Авивом:");
            System.out.println("Средняя цена: " + averagePrice);
            System.out.println("Медианная цена: " + medianPrice);
            System.out.println("Разница: " + (averagePrice - medianPrice));
        } else {
            System.err.println("Файл не найден: " + filePath);
        }
    }
}



