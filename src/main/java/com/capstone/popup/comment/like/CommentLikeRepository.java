package com.capstone.popup.comment.like;

import com.capstone.popup.comment.Comment;
import com.capstone.popup.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    List<CommentLike> findAllByCommentId(Comment comment);

    Optional<CommentLike> findByCommentIdAndMemberId(Comment comment, Member member);
}
