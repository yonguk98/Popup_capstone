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
    public void createStore(@RequestBody StoreCreateRequestDto dto){


    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping
    public GlobalResponse test(){
        return GlobalResponse.of("200","test");
    }
}
