package com.prog.core;

import com.prog.core.domain.TimestampCars;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public interface IInsights {

    Long getTotalCars(Stream<String> lineStream);

    HashMap<String, Long> getCarsPerDay(Stream<String> lineStream);

    List<TimestampCars> getSortedIntervals(Stream<String> lineStream);

    List<TimestampCars> getLeastIntervals(Stream<String> lineStream);
}
