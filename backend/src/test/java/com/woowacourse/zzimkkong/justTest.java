package com.woowacourse.zzimkkong;

import com.woowacourse.zzimkkong.domain.Reservation;
import com.woowacourse.zzimkkong.domain.Space;
import com.woowacourse.zzimkkong.repository.MapRepository;
import com.woowacourse.zzimkkong.repository.ReservationRepository;
import com.woowacourse.zzimkkong.repository.SpaceQueryRepository;
import com.woowacourse.zzimkkong.repository.SpaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.woowacourse.zzimkkong.CommonFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class justTest {
    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private SpaceQueryRepository spaceQueryRepository;

    @Test
    void findByIdTest() {
        spaceRepository.save(BE);
        spaceRepository.save(FE1);
        reservationRepository.save(BE_AM_ZERO_ONE);
        reservationRepository.save(BE_PM_ONE_TWO);

        System.out.println("========================시작2========================");
        List<Space> spacesList = spaceQueryRepository.findById(BE.getId(), BE_AM_ZERO_ONE.getStartTime(), BE_AM_ZERO_ONE.getStartTime().plusHours(4));
        System.out.println("========================끝========================");
        assertThat(spacesList.size()).isEqualTo(1);
        Space space = spacesList.get(0);
        List<Reservation> reservations = space.getReservations();
        assertThat(space.getReservations()).isEqualTo(reservations);
    }
}
