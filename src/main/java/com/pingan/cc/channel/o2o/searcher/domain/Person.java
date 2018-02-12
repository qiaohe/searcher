package com.pingan.cc.channel.o2o.searcher.domain;

/**
 * Created by HEQIAO939 on 2018/2/12.
 */
public class Person {
    private String name;
    private String city;
    private Double lat;
    private Double lng;

    public Person(String name, String city, Double lat, Double lng) {
        this.name = name;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Location getLocation() {
        return new Location(this.lat, this.lng);
    }
}
