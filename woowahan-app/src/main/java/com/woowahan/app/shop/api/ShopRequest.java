package com.woowahan.app.shop.api;

import com.woowahan.app.shop.domain.Shop;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShopRequest {

    private String shopNumber;
    private String address;
    private boolean isOpen;
    private String lat;
    private String lon;
    private int score;
    private String shopName;

    public Shop toShop() {
        return new Shop(shopNumber, address, isOpen, new Shop.Location(lat, lon), score, shopName);
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean getIsOpen() {
        return isOpen;
    }
}
