package com.capstone.popup.comment;

import com.capstone.popup.comment.like.CommentLike;
import com.capstone.popup.comment.like.CommentLikeCreateRequestDto;
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

    public void deleteCommentByStoreId(Long storeId) {
        commentRepository.deleteAllByStore(storeService.getStoreById(storeId));
    }

    public void deleteCommentByMemberId(Long memberId) {
        commentRepository.deleteAllByMember(memberService.getMemberById(memberId));
    }

    // paging 추가 필요
    public List<CommentReadResponseDto> getAllCommentsByStoreId(Long storeId, Principal principal) {
        List<Comment> commentList = commentRepository.findAllByStore(storeService.getStoreById(storeId));
        Member member = getLoginedMember(principal);

        List<CommentReadResponseDto> dtoList = new ArrayList<>();
        for (Comment comment : commentList) {

            // 좋아요 가능한지 아닌지 검증
            boolean likeable = true;
            if (member != null) {
                likeable = isLikeable(comment, member);
            }

            CommentReadResponseDto dto = CommentReadResponseDto.builder()
                    .id(comment.getId())
                    .writer(comment.getWriter())
                    .content(comment.getContent())
                    .likeCount(comment.getLikeCount())
                    .regDate(comment.getRegDate())
                    .likable(likeable)
                    .build();

            dtoList.add(dto);
        }
        return dtoList;
    }

    private boolean isLikeable(Comment comment, Member member) {
        Set<Long> likeIdSet = commentLikeService.getAllCommentLikeMemberIdByCommentId(comment);
        if (likeIdSet.contains(member.getId())) {
            return false;
        }
        return true;
    }

    private Member getLoginedMember(Principal principal) {
        try {
            String loginId = principal.getName();
            return memberService.getMemberByLoginId(loginId);
        } catch (Exception e) {
            return null;
        }
    }

    public Comment getCommentById(Long commnetId) {
        return commentRepository.findById(commnetId)
                .orElseThrow(() -> new EntityNotFoundException("후기를 찾을 수 없습니다."));
    }

    public void commentLike(CommentLikeCreateRequestDto dto) {

        Comment comment = getCommentById(dto.getCommentId());
        Member member = memberService.getMemberById(dto.getMemberId());

        // TODO: 이미 했는지 확인

        if (isLikeable(comment, member)) {
            commentLikeService.registerCommentLike(member, comment);

            comment.toBuilder()
                    .likeCount(comment.getLikeCount()+1)
                    .build();
            commentRepository.save(comment);
        }
    }

    public void cancelCommentLike(CommentLikeCreateRequestDto dto) {
        Comment comment = getCommentById(dto.getCommentId());
        Member member = memberService.getMemberById(dto.getMemberId());

        if (!isLikeable(comment, member)) {
            commentLikeService.deleteCommentLike(member, comment);

            if(comment.getLikeCount()>=1) {
                comment.toBuilder()
                        .likeCount(comment.getLikeCount() - 1)
                        .build();
                commentRepository.save(comment);
            }
        }
    }


}
