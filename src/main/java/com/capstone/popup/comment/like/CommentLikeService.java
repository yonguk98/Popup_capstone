package com.capstone.popup.comment.like;

import com.capstone.popup.comment.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    public Set<Long> getAllCommentLikeMemberIdByCommentId(Comment comment){

        return commentLikeRepository.findAllByCommentId(comment);
    }
}
