package org.example.ticketstest;

import java.io.File;
import java.util.*;

public class TicketAnalyzer {
    public static void main(String[] args) {
        // Путь к файлу
        Utils utils = new Utils();
        String filePath = utils.readFilePath("tickets.json");
        File file = new File(filePath);

        // Проверка на существование файла
        if (file.exists()) {
            // Создаем ObjectMapper для чтения JSON
            TicketWrapper wrapper = utils.readFile(file);
            // Получение списка билетов
            List<Ticket> tickets = wrapper.getTickets();
            // Фильтруем билеты по направлениям "Владивосток" -> "Тель-Авив"
            List<Ticket> filteredTickets = utils.filterTicketsAndPrint(tickets);
            // Минимальное время полета для каждого перевозчика
            Map<String, Long> minFlightTimeByCarrier = utils.getMinFlightTimeByCarrier(filteredTickets);
            // Вывод минимального времени полета для каждого перевозчика
            utils.printMinFlightTimeByCarrier(minFlightTimeByCarrier);
            // Получаем список цен на билеты и сортируем его
            List<Integer> prices = utils.getPrices(filteredTickets);
            // Печатем среднюю и медианную цену
             utils.printAverageAndMedian(prices);
        } else {
            System.err.println("Файл не найден: " + filePath);
        }
    }
}



