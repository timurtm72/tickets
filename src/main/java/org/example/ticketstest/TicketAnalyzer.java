package org.example.ticketstest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketAnalyzer {
    public static void main(String[] args) {
        // Путь к файлу
        Utils utils = new Utils();
        String filePath = utils.readFilePath("tickets.json",utils);
        File file = new File(filePath);

        // Проверка на существование файла
        if (file.exists()) {
            // Создаем ObjectMapper для чтения JSON
            TicketWrapper wrapper = utils.readFile(file);

            // Получение списка билетов
            List<Ticket> tickets = wrapper.getTickets();
            List<Ticket> filteredTickets = utils.filterTicketsAndPrint(tickets);
            // Минимальное время полета для каждого перевозчика
            Map<String, Long> minFlightTimeByCarrier = new HashMap<>();
            minFlightTimeByCarrier = utils.getMinFlightTimeByCarrier(filteredTickets);

            utils.printMinFlightTimeByCarrier(minFlightTimeByCarrier);
            // Получаем список цен на билеты и сортируем его
            List<Integer> prices = filteredTickets.stream().map(Ticket::getPrice).sorted().collect(Collectors.toList());

             utils.printAverageAndMedian(utils,prices);
        } else {
            System.err.println("Файл не найден: " + filePath);
        }
    }
}



