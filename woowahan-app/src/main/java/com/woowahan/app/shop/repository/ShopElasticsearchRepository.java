package com.woowahan.app.shop.repository;

import com.woowahan.app.shop.domain.Shop;
import com.woowahan.elasticsearch.shop.index.Location;
import com.woowahan.elasticsearch.shop.index.ShopInfo;
import com.woowahan.elasticsearch.shop.repository.ShopCommandClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ShopElasticsearchRepository implements ShopRepository {
    private final ShopCommandClient shopCommandClient;

    @Override
    public void save(Shop shop) {
        shopCommandClient.insert(convert(shop));
    }

    @Override
    public void update(Shop shop) {
        shopCommandClient.update(convert(shop));
    }

    @Override
    public void delete(String id) {
        shopCommandClient.delete(id);
    }
    private ShopInfo convert(Shop shop) {
        Shop.Location location = shop.getLocation();
        return new ShopInfo(
                shop.getShopNumber(),
                shop.getAddress(),
                shop.isOpen(),
                new Location(location.getLat(), location.getLon()),
                shop.getScore(),
                shop.getShopName()
        );
    }
}
