package com.capstone.popup.admin;

import com.capstone.popup.global.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminArticleController {

    private final AdminArticleService adminArticleService;

    @GetMapping
    public GlobalResponse getAllArticleList(){

        return GlobalResponse.of("200"
                ,"모든 게시글 반환"
                , adminArticleService.getAllArticle());
    }

    @GetMapping("/{id}")
    public GlobalResponse getOneArticle(@PathVariable Long id){

        return GlobalResponse.of("200"
                ,id + "번 게시글 반환"
                , adminArticleService.getArticleById(id));
    }

    @DeleteMapping("/{id}/delete")
    public GlobalResponse deleteArticle(@PathVariable Long id){

        adminArticleService.deleteArticle(id);

        return GlobalResponse.of("200", id + "번 게시글 삭제 완료");
    }


}
