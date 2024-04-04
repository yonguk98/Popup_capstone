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
    public void getAllStore(){

    }

    @GetMapping("/{id}")
    public void getOneStore(@PathVariable Long id){

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/test")
    public GlobalResponse test(){
        return GlobalResponse.of("200","test");
    }
}
