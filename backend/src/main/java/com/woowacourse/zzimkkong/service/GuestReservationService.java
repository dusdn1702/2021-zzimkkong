package com.woowacourse.zzimkkong.service;

import com.woowacourse.zzimkkong.domain.Reservation;
import com.woowacourse.zzimkkong.domain.Space;
import com.woowacourse.zzimkkong.dto.reservation.*;
import com.woowacourse.zzimkkong.exception.reservation.NoSuchReservationException;
import com.woowacourse.zzimkkong.exception.reservation.ReservationPasswordException;
import com.woowacourse.zzimkkong.exception.space.NoSuchSpaceException;
import com.woowacourse.zzimkkong.infrastructure.TimeConverter;
import com.woowacourse.zzimkkong.repository.MapRepository;
import com.woowacourse.zzimkkong.repository.ReservationRepository;
import com.woowacourse.zzimkkong.repository.SpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GuestReservationService extends ReservationService {
    public GuestReservationService(
            final MapRepository maps,
            final SpaceRepository spaces,
            final ReservationRepository reservations,
            final TimeConverter timeConverter) {
        super(maps, spaces, reservations, timeConverter);
    }

    public ReservationCreateResponse saveReservation(
            final Long mapId,
            final Long spaceId,
            final ReservationCreateUpdateWithPasswordRequest reservationCreateUpdateWithPasswordRequest) {
        validateMapExistence(mapId);
        validateTime(reservationCreateUpdateWithPasswordRequest);

        Space space = spaces.findByIdWithAfterTodayReservations(spaceId, reservationCreateUpdateWithPasswordRequest.getStartDateTime(), reservationCreateUpdateWithPasswordRequest.getEndDateTime())
                .orElse(Space.of(spaces.findById(spaceId).orElseThrow(NoSuchSpaceException::new), Collections.emptyList()));
        validateAvailability(space, reservationCreateUpdateWithPasswordRequest);

        Reservation reservation = reservations.save(
                new Reservation.Builder()
                        .startTime(reservationCreateUpdateWithPasswordRequest.getStartDateTime())
                        .endTime(reservationCreateUpdateWithPasswordRequest.getEndDateTime())
                        .password(reservationCreateUpdateWithPasswordRequest.getPassword())
                        .userName(reservationCreateUpdateWithPasswordRequest.getName())
                        .description(reservationCreateUpdateWithPasswordRequest.getDescription())
                        .space(space)
                        .build());

        return ReservationCreateResponse.from(reservation);
    }

    @Transactional(readOnly = true)
    public ReservationResponse findReservation(
            final Long mapId,
            final Long spaceId,
            final Long reservationId,
            final ReservationPasswordAuthenticationRequest reservationPasswordAuthenticationRequest) {
        validateMapExistence(mapId);
        validateSpaceExistence(spaceId);

        Reservation reservation = reservations
                .findById(reservationId)
                .orElseThrow(NoSuchReservationException::new);
        checkCorrectPassword(reservation, reservationPasswordAuthenticationRequest.getPassword());
        return ReservationResponse.from(reservation);
    }

    @Transactional(readOnly = true)
    public ReservationFindResponse findReservations(final Long mapId, final Long spaceId, final LocalDate date) {
        validateMapExistence(mapId);

        Space space = spaces.findByIdWithAfterTodayReservations(spaceId, date.atStartOfDay(), date.atStartOfDay().plusDays(1))
                .orElse(Space.of(spaces.findById(spaceId).orElseThrow(NoSuchSpaceException::new), Collections.emptyList()));
//        List<Reservation> reservations = getReservations(Collections.singletonList(space), date);
        List<Reservation> reservations = space.getReservations();

        return ReservationFindResponse.from(reservations);
    }

    @Transactional(readOnly = true)
    public ReservationFindAllResponse findAllReservations(final Long mapId, final LocalDate date) {
        validateMapExistence(mapId);

        List<Space> findSpaces = spaces.findAllWithReservationsAfterTime(mapId, date.atStartOfDay(), date.atStartOfDay().plusDays(1));
        if(findSpaces.isEmpty()) {
            findSpaces = spaces.findAllByMapId(mapId).stream()
                    .map(space -> Space.of(space, Collections.emptyList()))
                    .collect(Collectors.toList());
        }

        List<Reservation> reservations = findSpaces.stream()
                .flatMap(space -> space.getReservations().stream())
                .collect(Collectors.toList());

        return ReservationFindAllResponse.of(findSpaces, reservations);
    }

    public void updateReservation(
            final Long mapId,
            final Long spaceId,
            final Long reservationId,
            final ReservationCreateUpdateWithPasswordRequest reservationCreateUpdateWithPasswordRequest) {
        validateMapExistence(mapId);

        validateTime(reservationCreateUpdateWithPasswordRequest);

        Space space = spaces.findByIdWithAfterTodayReservations(spaceId, reservationCreateUpdateWithPasswordRequest.getStartDateTime(), reservationCreateUpdateWithPasswordRequest.getEndDateTime())
                .orElse(Space.of(spaces.findById(spaceId).orElseThrow(NoSuchSpaceException::new), Collections.emptyList()));
        Reservation reservation = reservations
                .findById(reservationId)
                .orElseThrow(NoSuchReservationException::new);

        checkCorrectPassword(reservation, reservationCreateUpdateWithPasswordRequest.getPassword());
        validateAvailability(space, reservationCreateUpdateWithPasswordRequest, reservation);

        reservation.update(reservationCreateUpdateWithPasswordRequest, space);
    }

    public void deleteReservation(
            final Long mapId,
            final Long spaceId,
            final Long reservationId,
            final ReservationPasswordAuthenticationRequest reservationPasswordAuthenticationRequest) {
        validateMapExistence(mapId);
        validateSpaceExistence(spaceId);

        Reservation reservation = reservations
                .findById(reservationId)
                .orElseThrow(NoSuchReservationException::new);
        checkCorrectPassword(reservation, reservationPasswordAuthenticationRequest.getPassword());
        reservations.delete(reservation);
    }

    private void checkCorrectPassword(final Reservation reservation, final String password) {
        if (reservation.isWrongPassword(password)) {
            throw new ReservationPasswordException();
        }
    }
}
