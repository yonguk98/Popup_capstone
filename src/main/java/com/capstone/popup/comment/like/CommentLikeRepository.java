package com.capstone.popup.comment.like;

import com.capstone.popup.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    public Set<Long> findAllByCommentId(Comment comment);
}
