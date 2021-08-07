package com.woowacourse.zzimkkong.repository;

import com.woowacourse.zzimkkong.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Long> {
    @Query(value = "select distinct s from Space s join fetch s.reservations r where s.id = :spaceId and r.endTime >= :time", nativeQuery = false)
    Optional<Space> findByIdWithAfterTodayReservations(@Param("spaceId") final Long spaceId, @Param("time") final LocalDateTime time);

    @Query(value = "select distinct s from Space s join fetch s.reservations r where s.map.id = :mapId and r.endTime >= :time", nativeQuery = false)
    List<Space> findAllWithReservationsAfterTime(@Param("mapId") final Long mapId, @Param("time") final LocalDateTime time);
}
