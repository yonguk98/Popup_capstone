package com.capstone.popup.comment.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommentLikeCreateRequestDto {

    private Long commentId;

    private Long memberId;
}
