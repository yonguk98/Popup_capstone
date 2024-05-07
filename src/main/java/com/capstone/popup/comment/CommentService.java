package com.capstone.popup.comment;

import com.capstone.popup.comment.like.CommentLike;
import com.capstone.popup.comment.like.CommentLikeRepository;
import com.capstone.popup.comment.like.CommentLikeService;
import com.capstone.popup.member.entity.Member;
import com.capstone.popup.member.service.MemberService;
import com.capstone.popup.store.Store;
import com.capstone.popup.store.StoreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final StoreService storeService;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentLikeService commentLikeService;

    public void registerComment(CommentCreateRequestDto dto) {

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

    public void deleteCommentById(Long commentId) {
        commentRepository.delete(getCommentById(commentId));
    }

    // paging 추가 필요
    public List<CommentReadResponseDto> getAllCommentsByStoreId(Long storeId, Principal principal) {
        List<Comment> commentList = commentRepository.findAllByStore(storeService.getStoreById(storeId));
        Member member = getLoginedMember(principal);

        List<CommentReadResponseDto> dtoList = new ArrayList<>();
        for(Comment comment : commentList){

            // 좋아요 가능한지 아닌지 검증
            boolean isLiked = false;
            if (member != null) {
                isLiked = isLikeable(comment, member);
            }

            CommentReadResponseDto dto = CommentReadResponseDto.builder()
                    .writer(comment.getWriter())
                    .content(comment.getContent())
                    .likeCount(comment.getLikeCount())
                    .regDate(comment.getRegDate())
                    .isLiked(isLiked)
                    .build();

            dtoList.add(dto);
        }
        return dtoList;
    }

    private boolean isLikeable(Comment comment, Member member) {
        Set<Long> likeIdSet = commentLikeService.getAllCommentLikeMemberIdByCommentId(comment);
        if(likeIdSet.contains(member.getId())){
            return true;
        }
        return false;
    }

    private Member getLoginedMember(Principal principal) {
        try {
            String loginId = principal.getName();
            return memberService.getMemberByLoginId(loginId);
        } catch (Exception e){
            return null;
        }
    }

    public Comment getCommentById(Long commnetId) {
        return commentRepository.findById(commnetId)
                .orElseThrow(() -> new EntityNotFoundException("후기를 찾을 수 없습니다."));
    }

    public void addLikeCountById(Long commentId){
        Comment comment = getCommentById(commentId);

        commentRepository.save(comment.toBuilder().likeCount(comment.getLikeCount()+1).build());
    }

    public void subLikeCountById(Long commentId){

    }


}
