package com.capstone.popup.comment;

import com.capstone.popup.member.entity.Member;
import com.capstone.popup.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByStore(Store store);

    Optional<Comment> findById(Long id);

    void deleteAllByStore(Store store);

    void deleteAllByMember(Member member);
}
