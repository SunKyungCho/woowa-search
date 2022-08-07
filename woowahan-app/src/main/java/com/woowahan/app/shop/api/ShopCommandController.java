package com.woowahan.app.shop.api;


import com.woowahan.app.shop.domain.Shop;
import com.woowahan.app.shop.service.command.ShopCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShopCommandController {

    private final ShopCommandService shopCommandService;;

    @PostMapping("/shops")
    public ResponseEntity<String> create(@RequestBody ShopRequest request) {
        Shop shop = request.toShop();
        shopCommandService.create(shop);
        return ResponseEntity.ok("create");
    }

    @PutMapping("/shops/{id}")
    public ResponseEntity<String> create(
            @PathVariable String id,
            @RequestBody ShopRequest request
    ) {
        Shop shop = request.toShop();
        shopCommandService.update(shop);
        return ResponseEntity.ok("update");
    }

    @DeleteMapping("/shops/{id}")
    public ResponseEntity<String> create(
            @PathVariable String id
    ) {
        shopCommandService.delete(id);
        return ResponseEntity.ok("delete");
    }
}
