package com.woowahan.elasticsearch.shop.index;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {

    private double lat;
    private double lon;

    public Location() {
    }

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
