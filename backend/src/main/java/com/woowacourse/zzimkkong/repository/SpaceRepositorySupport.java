package com.woowacourse.zzimkkong.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.zzimkkong.domain.QSpace;
import com.woowacourse.zzimkkong.domain.Space;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpaceRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public SpaceRepositorySupport(final JPAQueryFactory jpaQueryFactory) {
        super(Space.class);
        this.queryFactory = jpaQueryFactory;
    }
    // 서브쿼리 select * from space s left outer join (select * from reservation res where r.startTime >='2021-08-07' and res.end_date >= '2021-08-07') as r on r.space_id = s.id

    public List<Space> findById(final Long id) {
        QSpace space = QSpace.space;
        return queryFactory
                .selectFrom(space)
                .where(space.id.eq(id))
                .fetch();
    }
}
