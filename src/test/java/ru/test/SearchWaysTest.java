package ru.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.test.model.AeroportEnum;

class SearchWaysTest {

    private static String fileName = "C:\\Users\\user\\IdeaProjects\\test-Idea-Platform\\src\\main\\resources\\tickets.json";
    private static String formCity = AeroportEnum.VVO.getCity();
    private static String toCity = AeroportEnum.TLV.getCity();

    private SearchWays sw;

    @BeforeEach
    void setUp() {
        sw = new SearchWays(fileName, formCity, toCity);
    }

    @Test
    @DisplayName("Разница между средней ценой и медианой для полета между городами")
    void getDifferenceBetweenTheAveragePriceAndMedian() {
        double difference = sw.getDifferenceBetweenTheAveragePriceAndMedian();
        Assertions.assertThat(difference).isEqualTo(35);
    }

    @Test
    @DisplayName("Минимальное время полета между городами")
    void getMinFlightTimeBetweenCities() {
        int minTime = sw.getMinFlightTimeBetweenCities() / 60;
        Assertions.assertThat(minTime).isEqualTo(350);
    }
}