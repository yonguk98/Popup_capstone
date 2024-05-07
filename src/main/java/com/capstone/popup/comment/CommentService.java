package com.capstone.popup.comment;

import com.capstone.popup.member.entity.Member;
import com.capstone.popup.member.service.MemberService;
import com.capstone.popup.store.Store;
import com.capstone.popup.store.StoreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final StoreService storeService;

    public void registerComment(CommentCreateRequestDto dto){

        // null check and get entity
        Store store = storeService.getStoreById(dto.getStoreId());
        Member member = memberService.getMemberById(dto.getMemberId());

        Comment comment = Comment.builder()
                .store(store)
                .member(member)
                .writer(dto.getWriter())
                .content(dto.getContent())
                .regDate(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
    }

    public void deleteCommentById(Long commentId){
        commentRepository.delete(getCommentById(commentId));
    }

    public List<Comment> getAllCommentsByStoreId(Long storeId){
        return commentRepository.findAllByStore(storeService.getStoreById(storeId));
    }

    public Comment getCommentById(Long commnetId){
        return commentRepository.findById(commnetId)
                .orElseThrow(() -> new EntityNotFoundException("후기를 찾을 수 없습니다."));
    }


}
