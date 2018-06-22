package com.prog.core;

import com.prog.core.domain.TimestampCars;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrafficInsights implements IInsights {

    // assumptions
    // Minimum data size is equal to WINDOW size, in this case, 3
    // the timestamps are ordered in date and time
    // timestamps are in the interval of 0.5 hrs

    public final int INTERVAL_WINDOW = 3;
    private static int MAX_PARALLELISM = 5;
    private static String DELIM = " ";
    private static String TS_DELIM = "T";

    @Override
    public Long getTotalCars(Stream<String> lineStream) {
        Long total = lineStream.map((line) -> getCarsFromTsString(line)).reduce(0L, Long::sum);
        return total;
    }

    @Override
    public HashMap<String, Long> getCarsPerDay(Stream<String> lineStream) {

        return sumCarsPerDay(lineStream);
    }

    @Override
    public List<TimestampCars> getSortedIntervals(Stream<String> lineStream) {

        // sort the timestamp strings on car count.
        List<TimestampCars> sortedList = getTimestampCarsFromStream(lineStream)
                .sorted(Comparator.comparing(TimestampCars::getCars).reversed())
                .collect(Collectors.toList());

        return sortedList;
    }

    @Override
    public List<TimestampCars> getLeastIntervals(Stream<String> lineStream) {
        final Long[] lastLow = {Long.MAX_VALUE};
        List<String> lastNTimestamps = new ArrayList<String>();
        List<String> window = new ArrayList<String>();

        lineStream.forEachOrdered((String line) -> {
            lastNTimestamps.add(line);
            Long currLow;

            if (lastNTimestamps.size() >= INTERVAL_WINDOW) {
                currLow = lastNTimestamps.stream().map((temp) -> getCarsFromTsString(temp)).reduce(0L, Long::sum);

                if (currLow < lastLow[0]) {

                    if (timeStampInSequence(lastNTimestamps)) { // all dates are same
                        lastLow[0] = currLow;
                        window.clear();
                        for (String item : lastNTimestamps) {
                            window.add(item);
                        }
                    }
                }
                //shift window
                lastNTimestamps.remove(0);
            }
        });

        return getTimestampCarsFromStream(window.stream())
                .collect(Collectors.toList());
    }

    private Stream<TimestampCars> getTimestampCarsFromStream(Stream<String> lineStream) {
        return lineStream.map((line) -> {
            line = line.trim();
            int lastSpaceIndex = line.lastIndexOf(" ");
            Long carsNum = Long.parseLong(line.substring(lastSpaceIndex+1));
            String timestamp = line.substring(0, lastSpaceIndex);

            return new TimestampCars(timestamp, carsNum);
        });
    }

    private HashMap<String, Long> sumCarsPerDay(Stream<String> lineStream) {

        HashMap<String, Long> daysMap = new HashMap<String, Long>();

        lineStream.forEach((line) -> {
            line = line.trim();
            int lastSpaceIndex = line.lastIndexOf(DELIM);
            Long carsNum = Long.parseLong(line.substring(lastSpaceIndex+1));
            String timestamp = line.substring(0, lastSpaceIndex);

            String dateStr = timestamp.split(TS_DELIM)[0];

            Long lastValue = daysMap.get(dateStr);
            if(lastValue == null){
                lastValue = 0L;
            }
            lastValue += carsNum;
            daysMap.put(dateStr, lastValue);
        });
        return daysMap;
    }

    private DateTime getDateFromTimestamp(String timestamp) {
        DateTimeFormatter parser = ISODateTimeFormat.dateHourMinuteSecond();
        return parser.parseDateTime(timestamp);
    }

    private Long getCarsFromTsString(String ts){
        int lastSpaceIndex = ts.lastIndexOf(" ");
        return Long.parseLong(ts.substring(lastSpaceIndex + 1));
    }

    private String getTSFromTsString(String ts){
        int lastSpaceIndex = ts.lastIndexOf(" ");
        return ts.substring(0, lastSpaceIndex);
    }

    private Boolean timeStampInSequence(List<String> list) {

        List<DateTime> dtList = list.parallelStream().map((s) -> getDateFromTimestamp(getTSFromTsString(s))).collect(Collectors.toList());
        for(int i =0; i < dtList.size() - 1; ++i) {
            if(dtList.get(i).getMillis() + 30 * 60 * 1000 != dtList.get(i+1).getMillis()){
                return false;
            }
        }
        return true;
    }

//    public Long getTotalCarsParallel(Stream<String> lineStream) {
//        if(carsPerDay == null) {
//            sumCarsPerDay(lineStream);
//        }
//
//        if(carsPerDay != null) {
//            Long total = carsPerDay.reduceValuesToLong(MAX_PARALLELISM, (Long x) -> x.longValue(), 0L, Long::sum);
//            System.out.format("The number of cars seen in total = %d\n", total);
//        }
//    }
}
