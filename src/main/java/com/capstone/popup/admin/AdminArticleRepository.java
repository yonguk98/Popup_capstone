package com.capstone.popup.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminArticleRepository extends JpaRepository<AdminArticle,Long> {

    Optional<AdminArticle> findById(Long id);
}
