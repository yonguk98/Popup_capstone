package com.capstone.popup.store;

import com.capstone.popup.global.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public GlobalResponse getAllStore() {
        return GlobalResponse.of("200", "모든 스토어 조회", storeService.getAllStore());
    }

    @GetMapping("/{id}")
    public GlobalResponse getOneStore(@PathVariable Long id) {
        return GlobalResponse.of("200", id + "번 스토어 조회", storeService.getStoreById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/test")
    public GlobalResponse test() {
        return GlobalResponse.of("200", "test");
    }
}
