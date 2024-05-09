package com.capstone.popup.comment;

import com.capstone.popup.comment.like.CommentLikeCreateRequestDto;
import com.capstone.popup.global.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/register")
    public GlobalResponse registerComment(@RequestBody CommentCreateRequestDto dto) {

        commentService.registerComment(dto);

        return GlobalResponse.of("200", "후기 등록 성공");
    }

    @DeleteMapping("/delete/{commentId}")
    public GlobalResponse deleteComment(@PathVariable Long commentId) {
        commentService.deleteCommentById(commentId);

        return GlobalResponse.of("200", "후기 삭제 성공");
    }

    @GetMapping("/{articleId}")
    public GlobalResponse getAllComment(@PathVariable Long articleId, Principal principal) {

        List<CommentReadResponseDto> commentList = commentService.getAllCommentsByStoreId(articleId, principal);

        return GlobalResponse.of("200", "스토어 모든 후기 조회", commentList);
    }

    @PostMapping("/like")
    public GlobalResponse registerLike(@RequestBody CommentLikeCreateRequestDto dto) {

        commentService.commentLike(dto);

        return GlobalResponse.of("200", "좋아요 실행 성공");
    }

    @DeleteMapping("/like")
    public GlobalResponse deleteLike(@RequestBody CommentLikeCreateRequestDto dto) {

        commentService.cancelCommentLike(dto);

        return GlobalResponse.of("200", "좋아요 취소 실행 성공");
    }
}
