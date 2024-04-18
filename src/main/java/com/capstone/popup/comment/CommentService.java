package com.capstone.popup.comment;

import com.capstone.popup.member.service.MemberService;
import com.capstone.popup.store.StoreService;
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

        Comment comment = Comment.builder()
                .store(storeService.getStoreById(dto.getStoreId()))
                .member(memberService.getMemberById(dto.getMemberId()))
                .writer(dto.getWriter())
                .content(dto.getContent())
                .regDate(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsByStoreId(Long storeId){
        return commentRepository.findAllByStore(storeService.getStoreById(storeId));
    }




}
