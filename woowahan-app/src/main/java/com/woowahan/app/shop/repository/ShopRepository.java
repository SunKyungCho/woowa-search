package com.woowahan.app.shop.repository;

import com.woowahan.app.shop.domain.Shop;

public interface ShopRepository {
    void save(Shop shop);
    void update(Shop shop);
    void delete(String id);

}
