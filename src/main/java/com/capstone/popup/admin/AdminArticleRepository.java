package com.capstone.popup.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminArticleRepository extends JpaRepository<AdminArticle,Long> {
}
