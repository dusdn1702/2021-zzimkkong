package com.woowacourse.zzimkkong.repository;

import com.woowacourse.zzimkkong.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpaceRepository extends JpaRepository<Space, Long> {
    List<Space> findAllByMapId(final Long mapId);

    @Query(value = "select s from Space s join fetch s.reservations r where s.id = :spaceId and r.endTime > :time", nativeQuery = false)
    Optional<Space> findByIdWithAfterTodayReservations(final Long spaceId, final LocalDateTime time);

    @Query(value = "select distinct s from Space s join fetch s.reservations r where r.endTime > :time", nativeQuery = false)
    List<Space> findAllWithReservationsAfterTime(final LocalDateTime time);
}
