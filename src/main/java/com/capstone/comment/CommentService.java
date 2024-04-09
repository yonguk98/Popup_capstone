package com.capstone.comment;

import com.capstone.popup.member.entity.Member;
import com.capstone.popup.member.service.MemberService;
import com.capstone.popup.store.Store;
import com.capstone.popup.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final StoreService storeService;

    public void registerComment(CommentCreateRequestDto dto){

        Comment comment = Comment.builder()
                .store(storeService.getStoreById(dto.getStoreId()))
                .member(memberService.getMemberById(dto.getMemberId()))
                .writer(dto.getWriter())
                .content(dto.getContent())
                .regDate(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
    }

    public void getAllCommentsByStoreId(Long storeId){
    }


}