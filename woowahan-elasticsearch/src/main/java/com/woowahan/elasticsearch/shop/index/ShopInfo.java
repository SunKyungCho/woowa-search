package com.woowahan.elasticsearch.shop.index;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopInfo {

    private String shopNumber;
    private String address;
    private boolean isOpen;
    private Location location;
    private int score;
    private String shopName;

    public ShopInfo() {
    }

    public ShopInfo(String shopNumber, String address, boolean isOpen, Location location, int score, String shopName) {
        this.shopName = shopName;
        this.shopNumber = shopNumber;
        this.address = address;
        this.isOpen = isOpen;
        this.location = location;
        this.score = score;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean open) {
        isOpen = open;
    }
}
