package com.capstone.popup.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommentReadResponseDto {
    private Long id;
    
    private String writer;

    private String content;

    private Long likeCount;

    private LocalDateTime regDate;

    @Builder.Default
    private boolean likable = true;
}
