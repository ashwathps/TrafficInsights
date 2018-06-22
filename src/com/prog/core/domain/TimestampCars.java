package com.prog.core.domain;

public class TimestampCars {
    private String timestamp;
    private Long cars;

    public TimestampCars(String t, Long c) {
        this.timestamp = t;
        this.cars = c;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Long getCars() {
        return cars;
    }

    public void setCars(Long cars) {
        this.cars = cars;
    }
}
