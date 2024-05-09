package com.capstone.popup.comment.like;

import com.capstone.popup.comment.Comment;
import com.capstone.popup.comment.CommentService;
import com.capstone.popup.member.entity.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    public Set<Long> getAllCommentLikeMemberIdByCommentId(Comment comment) {

        List<CommentLike> commentLikeSet = commentLikeRepository.findAllByCommentId(comment);
        Set<Long> commentLikeMemberIdSet = new HashSet<>();
        for (CommentLike commentLike : commentLikeSet) {
            commentLikeMemberIdSet.add(commentLike.getMemberId().getId());
        }
        return commentLikeMemberIdSet;
    }

    public CommentLike getCommentLikeByCommentAndMember(Comment comment, Member member) {
        return commentLikeRepository.findByCommentIdAndMemberId(comment, member).orElseThrow(
                () -> new EntityNotFoundException()
        );
    }

    public void registerCommentLike(Member member, Comment comment) {

        // 중복체크는 comment service에서

        CommentLike commentLike = CommentLike.builder()
                .memberId(member)
                .commentId(comment)
                .build();

        commentLikeRepository.save(commentLike);
    }

    public void deleteCommentLike(Member member, Comment comment) {

        commentLikeRepository.delete(getCommentLikeByCommentAndMember(comment, member));
    }
}
