package com.woowacourse.zzimkkong.repository;

import com.woowacourse.zzimkkong.domain.Reservation;
import com.woowacourse.zzimkkong.domain.Space;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

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

        System.out.println("========================시작1========================");
        // when
        Optional<Space> actual = spaces.findByIdWithAfterTodayReservations(BE.getId(), THE_DAY_AFTER_TOMORROW_START_TIME, THE_DAY_AFTER_TOMORROW_START_TIME.plusDays(1).withHour(0).withMinute(0).withSecond(0));
        System.out.println("========================끝1========================");

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("reservations")
                .isEqualTo(List.of(BE, FE1));
    }
}
