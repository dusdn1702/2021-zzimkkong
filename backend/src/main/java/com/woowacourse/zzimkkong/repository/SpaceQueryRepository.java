package com.woowacourse.zzimkkong.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woowacourse.zzimkkong.domain.QSpace;
import com.woowacourse.zzimkkong.domain.Reservation;
import com.woowacourse.zzimkkong.domain.Space;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import static com.woowacourse.zzimkkong.domain.QSpace.space;
import static com.woowacourse.zzimkkong.domain.QReservation.reservation;

@Repository
public class SpaceQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public SpaceQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }
    // 서브쿼리 select * from space s left outer join (select * from reservation res where res.startTime >='2021-08-07' and res.end_date >= '2021-08-07') as r on r.space_id = s.id

    public List<Space> findById(final Long id, final LocalDateTime start, final LocalDateTime end) {
        return jpaQueryFactory.selectFrom(space).distinct()
                .leftJoin(ExpressionUtils.as(JPAExpressions.select(reservation).from(space)
                        .where(reservation.startTime.goe(start), reservation.endTime.loe(end)), "reservation"))
                .on(reservation.space.id.eq(space.id))
                .where(space.id.eq(id))
                .fetchResults()
                .getResults();
    }
}
