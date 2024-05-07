package com.capstone.popup.comment;

import com.capstone.popup.global.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/register")
    public GlobalResponse registerComment(@RequestBody CommentCreateRequestDto dto){

        commentService.registerComment(dto);

        return GlobalResponse.of("200","후기 등록 성공");
    }

    @DeleteMapping("/delete/{commentId}")
    public GlobalResponse deleteComment(@PathVariable Long commentId){
        commentService.deleteCommentById(commentId);

        return GlobalResponse.of("200", "후기 삭제 성공");
    }

    @GetMapping("/{articleId}")
    public GlobalResponse getAllComment(@PathVariable Long articleId){

        List<Comment> commentList = commentService.getAllCommentsByStoreId(articleId);
        // 엔티티 그대로 반환하지 않고 dto로 변환 필요.

        return GlobalResponse.of("200", "스토어 모든 후기 조회", commentList);
    }
}
