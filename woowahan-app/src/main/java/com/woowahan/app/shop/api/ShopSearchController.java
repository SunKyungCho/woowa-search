package com.woowahan.app.shop.api;


import com.woowahan.app.shop.domain.Shop;
import com.woowahan.app.shop.service.query.ShopSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShopSearchController {

    private final ShopSearchService shopSearchService;

    @GetMapping("/shops")
    public ResponseEntity<List<Shop>> search(
            @RequestParam(required = false) String shopName,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String filter
    ) {
        List<Shop> response = shopSearchService.search(shopName, sort, filter);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/shops/{id}")
    public ResponseEntity<Shop> searchById(
            @PathVariable String id
    ) {
        Shop response = shopSearchService.searchById(id);
        return ResponseEntity.ok(response);
    }
}
