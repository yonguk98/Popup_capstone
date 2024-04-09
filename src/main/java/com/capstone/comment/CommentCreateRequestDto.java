package com.capstone.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {

    private Long memberId;

    private Long storeId;

    private String writer;

    private String content;

}
