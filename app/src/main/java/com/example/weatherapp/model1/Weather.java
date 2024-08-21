package com.example.weatherapp.model1;

public class Weather{


    String id;
    String name;
    String temp;
    String description;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTemp() {
        return temp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
