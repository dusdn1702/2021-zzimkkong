package com.woowacourse.zzimkkong.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.zzimkkong.domain.QSpace;
import com.woowacourse.zzimkkong.domain.Space;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpaceQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public SpaceQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Space> findById(final Long id) {
        QSpace space = QSpace.space;
        return jpaQueryFactory
                .selectFrom(space)
                .where(space.id.eq(id))
                .fetch();
    }
}
