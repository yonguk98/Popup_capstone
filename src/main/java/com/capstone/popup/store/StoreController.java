package com.capstone.popup.store;

import com.capstone.popup.comment.CommentService;
import com.capstone.popup.global.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final CommentService commentService;

    @GetMapping
    public GlobalResponse getAllStore() {
        return GlobalResponse.of("200", "모든 스토어 조회", storeService.getAllStore());
    }

    @GetMapping("/{id}")
    public GlobalResponse getOneStore(@PathVariable Long id) {
        return GlobalResponse.of("200", id + "번 스토어 조회", storeService.getStoreById(id));
    }

    @DeleteMapping("/{id}")
    public GlobalResponse deleteStore(@PathVariable Long id) {

        storeService.deleteStoreById(id);
        commentService.deleteCommentByStoreId(id);
        return GlobalResponse.of("200", id + "번 스토어 삭제 성공");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/test")
    public GlobalResponse test() {
        return GlobalResponse.of("200", "test");
    }

    @GetMapping("/geocode")
    public GlobalResponse geocode(@RequestParam String addr) {

        String res = storeService.requestGeocode(addr);

        return GlobalResponse.of("200","주소 좌표 변환 완료", res);
    }
}
