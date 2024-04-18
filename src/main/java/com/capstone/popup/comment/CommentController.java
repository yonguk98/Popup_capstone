package com.capstone.popup.comment;

import com.capstone.popup.global.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/register")
    public GlobalResponse registerComment(@RequestBody CommentCreateRequestDto dto){



        return GlobalResponse.of("200","후기 등록 성공");
    }
}
