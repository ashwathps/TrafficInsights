package com.prog.test;

import com.prog.core.IInsights;
import com.prog.core.TrafficInsights;
import com.prog.core.domain.TimestampCars;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class TrafficInsightsTest {

    @Test
    public void testTotalCarsWhenStreamEmpty() {

        IInsights trafficInsights = new TrafficInsights();

        assertThat(trafficInsights.getTotalCars(Stream.empty())).isEqualTo(0);

    }

    @Test
    public void testTotalCarsWhenStreamIsClosed() throws IllegalStateException{

        IInsights trafficInsights = new TrafficInsights();

        Stream<String> stream = Stream.empty();
        stream.close();

        try {
            trafficInsights.getTotalCars(stream);
        }catch (Exception ex) {
            assertTrue(ex instanceof IllegalStateException);
        }
    }

    @Test
    public void testTotalCars() {

        IInsights trafficInsights = new TrafficInsights();

        List<String> data = new ArrayList<String>();
        data.add("2016-12-05T12:30:00 6");
        data.add("2016-12-08T19:00:00 28");
        data.add("2016-12-08T23:00:00 11");

        assertThat(trafficInsights.getTotalCars(data.stream())).isEqualTo(45);
    }

    @Test
    public void testCarsPerDayWhenOneDayIsGiven() {
        IInsights trafficInsights = new TrafficInsights();

        List<String> data = new ArrayList<String>();
        data.add("2016-12-05T12:30:00 6");
        data.add("2016-12-05T19:00:00 28");
        data.add("2016-12-05T23:00:00 11");

        assertThat(trafficInsights.getCarsPerDay(data.stream()).size()).isEqualTo(1);
    }

    @Test
    public void testCarsPerDayWhenInvalidFormattedDataIsGiven() throws NumberFormatException{
        IInsights trafficInsights = new TrafficInsights();

        List<String> data = new ArrayList<String>();
        data.add("2016-12-0512:30:00 6");
        data.add("2016-12-05T19:00:0028");
        data.add("2016-12-05T23:00:00 11");

        try{
            trafficInsights.getCarsPerDay(data.stream());
        }catch (Exception ex) {
            assertTrue(ex instanceof NumberFormatException);
        }
    }

    @Test
    public void testSortedIntervalOfCars() {
        IInsights trafficInsights = new TrafficInsights();

        List<String> data = new ArrayList<String>();
        data.add("2016-12-05T12:30:00 6");
        data.add("2016-12-05T19:00:00 28");
        data.add("2016-12-05T23:00:00 11");
        data.add("2016-12-09T00:00:00 4");

        assertThat(trafficInsights.getSortedIntervals(data.stream()).get(0).getCars()).isEqualTo(28);
        assertThat(trafficInsights.getSortedIntervals(data.stream()).get(3).getCars()).isEqualTo(4);
    }

    @Test
    public void testLowestIntervalsOfCars() {
        IInsights trafficInsights = new TrafficInsights();

        List<String> data = new ArrayList<String>();
        data.add("2016-12-05T12:00:00 22");
        data.add("2016-12-05T12:30:00 6");
        data.add("2016-12-05T13:00:00 28");
        data.add("2016-12-05T13:30:00 11");
        data.add("2016-12-09T20:00:00 4");

        List<TimestampCars> window = trafficInsights.getLeastIntervals(data.stream());

//        for (int i = 0; i < window.size(); i++) {
//            System.out.format("%s %d\n", window.get(i).getTimestamp(), window.get(i).getCars());
//        }
        assertThat(window.get(0).getCars()).isEqualTo(6);
        assertThat(window.get(2).getCars()).isEqualTo(11);
    }

    @Test
    public void testLowestIntervalsOfCarsAcrossDates() {
        IInsights trafficInsights = new TrafficInsights();

        List<String> data = new ArrayList<String>();
        data.add("2016-12-05T12:00:00 22");
        data.add("2016-12-05T12:30:00 6");
        data.add("2016-12-05T23:30:00 1");
        data.add("2016-12-06T00:00:00 1");
        data.add("2016-12-06T00:30:00 1");

        List<TimestampCars> window = trafficInsights.getLeastIntervals(data.stream());

        assertThat(window.get(0).getCars()).isEqualTo(1);
        assertThat(window.get(2).getCars()).isEqualTo(1);
    }

    @Test
    public void testLowestIntervalsOfCarsAcrossDates2() {
        IInsights trafficInsights = new TrafficInsights();

        List<String> data = new ArrayList<String>();
        data.add("2016-12-05T12:00:00 22");
        data.add("2016-12-05T12:30:00 6");
        data.add("2016-12-05T23:30:00 1");
        data.add("2016-12-06T00:30:00 1");

        List<TimestampCars> window = trafficInsights.getLeastIntervals(data.stream());

//        for (int i = 0; i < window.size(); i++) {
//            System.out.format("%s %d\n", window.get(i).getTimestamp(), window.get(i).getCars());
//        }

        assertThat(window.size()).isEqualTo(0);
    }

    @Test
    public void testLowestIntervalsOfCarsAcrossDatesNoWindow() {
        IInsights trafficInsights = new TrafficInsights();

        List<String> data = new ArrayList<String>();
        data.add("2016-12-05T12:00:00 22");
        data.add("2016-12-05T23:30:00 1");
        data.add("2016-12-06T00:30:00 1");

        List<TimestampCars> window = trafficInsights.getLeastIntervals(data.stream());

        assertThat(window.size()).isEqualTo(0);
    }

    @Test
    public void testLowestIntervalsOfCarsInsifficientData() {
        IInsights trafficInsights = new TrafficInsights();

        List<String> data = new ArrayList<String>();
        data.add("2016-12-05T12:00:00 22");
        data.add("2016-12-05T12:30:00 6");

        List<TimestampCars> window = trafficInsights.getLeastIntervals(data.stream());
        assertThat(window.size()).isEqualTo(0);

    }
}
