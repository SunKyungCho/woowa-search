package com.woowahan.app.shop.service.command;

import com.woowahan.app.shop.domain.Shop;
import com.woowahan.app.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopCommandService {

    private final ShopRepository shopRepository;

    public void create(Shop shop) {
        shopRepository.save(shop);
    }

    public void update(Shop shop) {
        shopRepository.update(shop);
    }

    public void delete(String id) {
        shopRepository.delete(id);
    }

}
