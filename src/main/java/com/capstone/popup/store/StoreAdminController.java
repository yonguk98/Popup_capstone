package com.capstone.popup.store;

import com.capstone.popup.global.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/store")
public class StoreAdminController {

    private final StoreService storeService;

    @PostMapping("/create")
    public GlobalResponse createStore(@RequestBody StoreCreateRequestDto dto) {
        storeService.registerStore(dto);

        return GlobalResponse.of("200", "스토어 등록 성공");
    }

    @PostMapping("/{id}/update")
    public GlobalResponse updateStore(@PathVariable Long id, @RequestBody StoreCreateRequestDto dto) {
        storeService.updateStoreById(id, dto);

        return GlobalResponse.of("200", "스토어 수정 성공");
    }

    @DeleteMapping("/{id}/delete")
    public GlobalResponse deleteStore(@PathVariable Long id) {
        storeService.deleteStoreById(id);

        return GlobalResponse.of("200", "스토어 삭제 성공");
    }

    @GetMapping
    public GlobalResponse test() {
        return GlobalResponse.of("200", "test");
    }
}
