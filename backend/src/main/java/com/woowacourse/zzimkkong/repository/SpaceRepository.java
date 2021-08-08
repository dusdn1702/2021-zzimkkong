package com.woowacourse.zzimkkong.repository;

import com.woowacourse.zzimkkong.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceRepository extends JpaRepository<Space, Long> {
//    @Query(value = "select distinct s from Space s join fetch s.reservations r where s.id = :spaceId and r.startTime >=:now and r.endTime <= :endTime", nativeQuery = false)
//    Optional<Space> findByIdWithAfterTodayReservations(@Param("spaceId") final Long spaceId, @Param("now") final LocalDateTime now, @Param("endTime") final LocalDateTime endTime);

//    @Query(value = "select distinct s from Space s join fetch s.reservations r where s.map.id = :mapId and r.startTime >=:now and r.endTime <= :endTime", nativeQuery = false)
//    List<Space> findAllWithReservationsAfterTime(@Param("mapId") final Long mapId, @Param("now") final LocalDateTime now, @Param("endTime") final LocalDateTime endTime);

    List<Space> findAllByMapId(Long mapId);
}

