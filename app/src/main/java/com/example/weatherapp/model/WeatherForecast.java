package com.example.weatherapp.model;

public class WeatherForecast {
    private String time;
    private String temperature;
    private String icon;

    public WeatherForecast(String time, String temperature, String icon) {
        this.icon = icon;
        this.time = time;
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
