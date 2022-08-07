package com.woowahan.app.shop.domain;

import lombok.Getter;

@Getter
public class Shop {

    private String shopNumber;
    private String address;
    private boolean isOpen;
    private Location location;
    private int score;
    private String shopName;

    public Shop(String shopNumber, String address, boolean isOpen, Location location, int score, String shopName) {
        this.shopName = shopName;
        this.shopNumber = shopNumber;
        this.address = address;
        this.isOpen = isOpen;
        this.location = location;
        this.score = score;
    }


    @Getter
    public static class Location {
        private double lat;
        private double lon;

        public Location(String lat, String lon) {
            this.lat = lat != null && lat.length() != 0 ? Double.parseDouble(lat) : 0;
            this.lon = lon != null && lon.length() != 0 ? Double.parseDouble(lon) : 0;
        }
    }
}
