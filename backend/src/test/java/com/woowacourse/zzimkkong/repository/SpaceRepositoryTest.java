package com.woowacourse.zzimkkong.repository;

import com.woowacourse.zzimkkong.domain.Reservation;
import com.woowacourse.zzimkkong.domain.Space;
import com.woowacourse.zzimkkong.exception.space.NoSuchSpaceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.woowacourse.zzimkkong.CommonFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class SpaceRepositoryTest extends RepositoryTest {
    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        POBI = members.save(POBI);
        LUTHER = maps.save(LUTHER);
        BE = spaces.save(BE);
        FE1 = spaces.save(FE1);

        Reservation BE_YESTERDAY = new Reservation.Builder()
                .startTime(THE_DAY_AFTER_TOMORROW_START_TIME.minusDays(2))
                .endTime(THE_DAY_AFTER_TOMORROW_START_TIME.minusDays(2).plusHours(1))
                .description("옛날 회의")
                .userName(USER_NAME)
                .password(RESERVATION_PASSWORD)
                .space(BE)
                .build();

        reservations.save(BE_YESTERDAY);
        reservations.save(BE_AM_ZERO_ONE);
        reservations.save(BE_PM_ONE_TWO);
        reservations.save(BE_NEXT_DAY_PM_SIX_TWELVE);
        reservations.save(FE1_ZERO_ONE);

        entityManager.flush();
        entityManager.clear();
    }

    @DisplayName("공간을 저장한다.")
    @Test
    void save() {
        // given, when

        // then
        assertThat(BE.getId()).isNotNull();
        assertThat(BE).usingRecursiveComparison()
                .isEqualTo(BE);
    }

    @DisplayName("맵의 Id를 이용해 모든 공간을 찾아온다.")
    @Test
    void findAllByMapId() {

        // when
        List<Space> actual = spaces.findAllWithReservationsAfterTime(LUTHER.getId(), THE_DAY_AFTER_TOMORROW_START_TIME, THE_DAY_AFTER_TOMORROW_START_TIME.plusDays(1).withHour(0).withMinute(0).withSecond(0));

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("reservations")
                .isEqualTo(List.of(BE, FE1));
    }

    @Test
    @Disabled
    @Transactional
    void findByIdTest() {
        Space be = spaces.findByIdWithAfterTodayReservations(1L, THE_DAY_AFTER_TOMORROW_START_TIME, THE_DAY_AFTER_TOMORROW_START_TIME.plusDays(1).withHour(0).withMinute(0).withSecond(0))
                .orElseThrow(IllegalAccessError::new);
//                .orElse(Space.of(spaces.findById(1L).orElseThrow(NoSuchSpaceException::new), Collections.emptyList()));
//                .orElse(spaces.findByIdWithEmptyReservations(1L).orElseThrow(NoSuchSpaceException::new));

        List<Reservation> reservations1 = be.getReservations();
        System.out.println(reservations1);
        Space bePersisted = spaces.findById(1L).orElseThrow(NoSuchSpaceException::new);

//        List<Space> allWithAfterTodayReservations = spaces.findAllWithReservationsAfterTime(LocalDateTime.now());
        System.out.println("-----------------비교를 해보아요------------------");
//        List<Reservation> allBySpaceIdAndEndTimeAfter = reservations.findAllBySpaceIdAndEndTimeAfter(1L, LocalDateTime.now());

        System.out.println("-----------------요까지------------------");
        System.out.println("be = " + be.getName());

        List<Reservation> reservations = be.getReservations();
        for (Reservation reservation: be.getReservations()) {
            System.out.println("reservation = " + reservation);
        }
//        System.out.println("----------------------find all----------------------");
//        List<Space> all = entityManager.createQuery("select distinct s from Space s join fetch s.reservations", Space.class)
//                .getResultList();
//
//        System.out.println("----------------------루프 전----------------------");
//
//        for (Space space: all) {
//            System.out.println("----------------------1단 for문----------------------");
//            for (Reservation reservation: space.getReservations()) {
//                System.out.println("----------------------2단 for문----------------------");
//                System.out.println("이 예약은 " + reservation.getDescription() + "라는 설명을 가졌네요.");
//            }
//        }

    }
}
