package com.capstone.popup.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminArticle {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();
}
