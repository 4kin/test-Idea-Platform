package ru.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import ru.test.deserializer.TicketDeserializerFromJson;
import ru.test.model.AeroportEnum;
import ru.test.model.Ticket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchWays {
    private List<Ticket> ticketList;
    private String fromCity;
    private String toCity;

    public SearchWays(String fileName, String formCity, String toCity) {
        this.ticketList = LoadTicketsFormJson(new File(fileName));
        this.fromCity = formCity;
        this.toCity = toCity;
    }

    public static void main(String[] args) {
        String fileName;
        String formCity;
        String toCity;
        if (args.length != 3) {
            System.out.println("Вызовете программу с параметрами путь_к_файлу_json город_откуда город_куда");
            System.out.println("Использованы переменные по умолчанию tickets.json Владивосток Тель-Авив");
            System.out.println();
            fileName = "src/main/resources/tickets.json";
            formCity = AeroportEnum.VVO.getCity();
            toCity = AeroportEnum.TLV.getCity();
        } else {
            fileName = args[0];
            formCity = args[1];
            toCity = args[2];
        }

        SearchWays sw = new SearchWays(fileName, formCity, toCity);

        // вывод ответа на первый вопрос
        sw.printResultForMinPrice(sw.getMinFlightTimeBetweenCitiesByCarrier());

        // вывод ответа на второй вопрос
        double difference = sw.getDifferenceBetweenTheAveragePriceAndMedian();
        DecimalFormat decimalFormat = new DecimalFormat("# ###.##руб");
        System.out.printf("Разница = %s между средней ценой и медианой для полета между городами %s и %s", decimalFormat.format(difference), formCity, toCity);

    }

    private static int duration(Ticket ticket) {
        int minutes = (int) Duration.between(ticket.getDepartureDateTime().toInstant(), ticket.getArrivalDateTime().toInstant()).toMinutes();
        return minutes;
    }

    public double getDifferenceBetweenTheAveragePriceAndMedian() {
        double averagePrice = ticketList
                .stream()
                .filter(ticket ->
                        ticket.getOriginName().equals(fromCity)
                        && ticket.getDestinationName().equals(toCity))
                .mapToInt(ticket -> ticket.getPrice().intValue())
                .average()
                .orElse(0);

        List<Integer> pricesList = ticketList
                .stream()
                .filter(ticket ->
                        ticket.getOriginName().equals(fromCity)
                        && ticket.getDestinationName().equals(toCity))
                .mapToInt(ticket -> ticket.getPrice().intValue())
                .sorted()
                .boxed()
                .collect(Collectors.toList());

        int size = pricesList.size();
        double median;
        if (size % 2 == 0) {
            // четно
            int middle = size / 2 - 1;
            median = (pricesList.get(middle) + pricesList.get(middle + 1)) / 2;
        } else {
            // нечетно
            int middle = (size) / 2;
            median = pricesList.get(middle);
        }

        return averagePrice - median;
    }

    public List<Ticket> LoadTicketsFormJson(File file) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Ticket.class, new TicketDeserializerFromJson())
                .setPrettyPrinting()
                .create();

        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        JsonElement jsonElement = JsonParser.parseReader(reader);
        List<JsonElement> tickets = jsonElement.getAsJsonObject().getAsJsonArray("tickets").asList();
        List<Ticket> ticketList = new ArrayList<>();
        for (JsonElement ticketElement : tickets) {
            ticketList.add(gson.fromJson(ticketElement, Ticket.class));
        }
        return ticketList;
    }

    public int getMinFlightTimeBetweenCities() {
        return ticketList
                .stream()
                .filter(ticket ->
                        ticket.getOriginName().equals(fromCity)
                        && ticket.getDestinationName().equals(toCity))
                .mapToInt(SearchWays::duration)
                .min()
                .getAsInt();
    }


    public Map<String, Integer> getMinFlightTimeBetweenCitiesByCarrier() {
        Map<String, List<Ticket>> ticketsByCarrierMap = ticketList
                .stream()
                .filter(ticket ->
                        ticket.getOriginName().equals(fromCity)
                        && ticket.getDestinationName().equals(toCity))
                .collect(Collectors.groupingBy(Ticket::getCarrier));

        Map<String, Integer> minPrice = new HashMap<>();
        for (Map.Entry<String, List<Ticket>> item : ticketsByCarrierMap.entrySet()) {
            minPrice.put(
                    item.getKey(),
                    item.getValue()
                            .stream()
                            .mapToInt(SearchWays::duration)
                            .min()
                            .orElse(0));
        }
        return minPrice;
    }

    public void printResultForMinPrice(Map<String, Integer> minPrice) {

        for (Map.Entry<String, Integer> item : minPrice.entrySet()) {
            System.out.printf("Минимальное время полета = %s (в минутах) для перевозчика %s между %s и %s\n", item.getValue(), item.getKey(), this.fromCity, this.toCity);
        }

    }


}
