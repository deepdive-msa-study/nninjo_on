package com.example.msablog.post.repository;

import com.example.msablog.post.dto.ReadPostResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.msablog.post.entity.QPost.post;

@Repository
public class PostJQueryRepository {
    private final JPAQueryFactory queryFactory;

    public PostJQueryRepository(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    public Page<ReadPostResponseDto> findAll(Pageable pageable) {

        List<ReadPostResponseDto> dtoList = queryFactory
                .select(
                        Projections.constructor(
                                ReadPostResponseDto.class,
                                post.id,
                                post.title,
                                post.content,
                                post.author
                        )
                )
                .from(post)
                .orderBy(post.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(post)
                .fetch().size();

        return new PageImpl<>(dtoList, pageable, total);
    }
}
