package com.capstone.popup.admin;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminArticleService {

    private final AdminArticleRepository adminArticleRepository;


    public void createArticle(String content) {

        if (content == null) {
            throw new NullPointerException("게시글 내용이 존재하지 않습니다.");
        }

        AdminArticle adminArticle = AdminArticle.builder()
                .content(content)
                .build();

        adminArticleRepository.save(adminArticle);
    }

    public AdminArticle getArticleById(Long id) {
        Optional<AdminArticle> adminArticleOp = adminArticleRepository.findById(id);

        if (adminArticleOp.isPresent()) {
            return adminArticleOp.get();
        } else {
            throw new EntityNotFoundException("게시글이 존재하지 않습니다.");
        }
    }

    public List<AdminArticle> getAllArticle(){
        // null 체크 해야하나?
        return adminArticleRepository.findAll();
    }

    public void deleteArticle(Long id) {
        AdminArticle article = getArticleById(id);

        adminArticleRepository.delete(article);
    }

}
