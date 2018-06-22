package com.prog.core;

import com.prog.core.domain.TimestampCars;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Presenter {
    
    public static void main(String[] args) {
        String fileName = args[0];

        Supplier<Stream<String>> streamSupplier = () -> {
            Stream<String> lines = null;
            try {
                lines = Files.lines(Paths.get(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lines;
        };

        IInsights trafficInsights = new TrafficInsights();
        int interval_size = 3;

        Long total = trafficInsights.getTotalCars(streamSupplier.get());
        System.out.format("The number of cars seen in total = %d\n", total);

        System.out.println("The number of cars seen per day");
        HashMap<String, Long> carsPerDay = trafficInsights.getCarsPerDay(streamSupplier.get());
        carsPerDay.forEach((day, value) -> {
            System.out.format("%s  %d \n", day, value);
        });

        System.out.format("The top %d intervals of cars\n", interval_size);
        List<TimestampCars> sortedList = trafficInsights.getSortedIntervals(streamSupplier.get());
        for (int i = 0; i < interval_size; i++) {
            System.out.format("%s %d\n", sortedList.get(i).getTimestamp(), sortedList.get(i).getCars());
        }

        System.out.format("The lowest %d contiguous intervals are\n", interval_size);
        List<TimestampCars> window = trafficInsights.getLeastIntervals(streamSupplier.get());
        for (int i = 0; i < window.size(); i++) {
            System.out.format("%s %d\n", window.get(i).getTimestamp(), window.get(i).getCars());
        }
    }
}
