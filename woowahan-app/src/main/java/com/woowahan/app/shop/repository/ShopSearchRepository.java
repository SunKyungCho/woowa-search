package com.woowahan.app.shop.repository;

import com.woowahan.app.shop.domain.Shop;

import java.util.List;

public interface ShopSearchRepository {


    List<Shop> search(String shopName, String sort, String filter);

    Shop searchById(String id);
}
