package com.example.msablog.post.entity;

import com.example.msablog.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
