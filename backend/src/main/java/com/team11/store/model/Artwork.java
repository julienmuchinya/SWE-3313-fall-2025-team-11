package com.team11.store.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Artwork {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String artist;
    private String description;
    private double price;
    private boolean sold = false;

    public Artwork() {}

    public Artwork(String title, String artist, String description, double price) {
        this.title = title;
        this.artist = artist;
        this.description = description;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isSold() { return sold; }
    public void setSold(boolean sold) { this.sold = sold; }
}
