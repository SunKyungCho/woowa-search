package com.woowahan.app.shop.service.query;


import com.woowahan.app.shop.domain.Shop;
import com.woowahan.app.shop.repository.ShopSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopSearchService {

    private final ShopSearchRepository shopSearchRepository;

    public List<Shop> search(String shopName, String sort, String filter) {
        return shopSearchRepository.search(shopName, sort, filter);
    }

    public Shop searchById(String id) {
        return shopSearchRepository.searchById(id);
    }
}
