package com.nninjoon.postservice.entity;

import java.time.LocalDateTime;

import com.example.msablog.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String content;
    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime updatedAt;

    private String userId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
