package com.pandey.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
@Embeddable
public class Item {
    private String name;
    @Column(name = "colour")
    private String colour;
    private char gender_recommendation;
    private char size;
    private double price, rating;
    private String availability;

    public Item(){

    }


    public Item(String name, String colour, char gender_recommendation, char size, double price, double rating, String availability) {
        this.name = name;
        this.colour = colour;
        this.gender_recommendation = gender_recommendation;
        this.size = size;
        this.price = price;
        this.rating = rating;
        this.availability = availability;
    }

    

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return this.colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public char getGender_recommendation() {
        return this.gender_recommendation;
    }

    public void setGender_recommendation(char gender_recommendation) {
        this.gender_recommendation = gender_recommendation;
    }

    public char getSize() {
        return this.size;
    }

    public void setSize(char size) {
        this.size = size;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getAvailability() {
        return this.availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", colour='" + getColour() + "'" +
            ", gender_recommendation='" + getGender_recommendation() + "'" +
            ", size='" + getSize() + "'" +
            ", price='" + getPrice() + "'" +
            ", rating='" + getRating() + "'" +
            ", availability='" + getAvailability() + "'" +
            "}";
    }



}
