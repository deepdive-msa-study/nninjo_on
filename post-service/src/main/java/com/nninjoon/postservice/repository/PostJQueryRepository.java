package com.nninjoon.postservice.repository;

import static com.nninjoon.postservice.entity.QPost.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.nninjoon.postservice.dto.PostResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;


@Repository
public class PostJQueryRepository {
    private final JPAQueryFactory queryFactory;

    public PostJQueryRepository(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    public Page<PostResponse> findAll(Pageable pageable) {

        List<PostResponse> dtoList = queryFactory
                .select(
                        Projections.constructor(
                                PostResponse.class,
                                post.id,
                                post.title,
                                post.content,
                                post.userId
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
