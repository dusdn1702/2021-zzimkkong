package com.woowacourse.zzimkkong.service;

import com.woowacourse.zzimkkong.domain.Map;
import com.woowacourse.zzimkkong.domain.Member;
import com.woowacourse.zzimkkong.domain.Setting;
import com.woowacourse.zzimkkong.domain.Space;
import com.woowacourse.zzimkkong.dto.space.*;
import com.woowacourse.zzimkkong.exception.authorization.NoAuthorityOnMapException;
import com.woowacourse.zzimkkong.exception.map.NoSuchMapException;
import com.woowacourse.zzimkkong.exception.space.NoSuchSpaceException;
import com.woowacourse.zzimkkong.exception.space.ReservationExistOnSpaceException;
import com.woowacourse.zzimkkong.infrastructure.TimeConverter;
import com.woowacourse.zzimkkong.repository.MapRepository;
import com.woowacourse.zzimkkong.repository.ReservationRepository;
import com.woowacourse.zzimkkong.repository.SpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SpaceService {
    private final MapRepository maps;
    private final SpaceRepository spaces;

    public SpaceService(
            final MapRepository maps,
            final SpaceRepository spaces) {
        this.maps = maps;
        this.spaces = spaces;
    }

    public SpaceCreateResponse saveSpace(
            final Long mapId,
            final SpaceCreateUpdateRequest spaceCreateUpdateRequest,
            final Member manager) {
        Map map = maps.findById(mapId)
                .orElseThrow(NoSuchMapException::new);
        validateAuthorityOnMap(manager, map);

        SettingsRequest settingsRequest = spaceCreateUpdateRequest.getSettingsRequest();

        Setting setting = new Setting.Builder()
                .availableStartTime(settingsRequest.getAvailableStartTime())
                .availableEndTime(settingsRequest.getAvailableEndTime())
                .reservationTimeUnit(settingsRequest.getReservationTimeUnit())
                .reservationMinimumTimeUnit(settingsRequest.getReservationMinimumTimeUnit())
                .reservationMaximumTimeUnit(settingsRequest.getReservationMaximumTimeUnit())
                .reservationEnable(settingsRequest.getReservationEnable())
                .disabledWeekdays(settingsRequest.getDisabledWeekdays())
                .build();

        Space space = spaces.save(
                new Space.Builder()
                        .name(spaceCreateUpdateRequest.getSpaceName())
                        .color(spaceCreateUpdateRequest.getColor())
                        .description(spaceCreateUpdateRequest.getDescription())
                        .area(spaceCreateUpdateRequest.getArea())
                        .setting(setting)
                        .map(map)
                        .textPosition(null)
                        .coordinate(null)
                        .build());
        return SpaceCreateResponse.from(space);
    }

    @Transactional(readOnly = true)
    public SpaceFindDetailResponse findSpace(
            final Long mapId,
            final Long spaceId,
            final Member manager) {
        Map map = maps.findById(mapId)
                .orElseThrow(NoSuchMapException::new);
        validateAuthorityOnMap(manager, map);

        Space space = spaces.findById(spaceId).orElseThrow(NoSuchSpaceException::new);
//                .orElse(Space.of(spaces.findById(spaceId).orElseThrow(NoSuchSpaceException::new), Collections.emptyList()));
        return SpaceFindDetailResponse.from(space);
    }

    @Transactional(readOnly = true)
    public SpaceFindAllResponse findAllSpace(
            final Long mapId,
            final Member manager) {
        Map map = maps.findById(mapId)
                .orElseThrow(NoSuchMapException::new);
        validateAuthorityOnMap(manager, map);

        List<Space> findAllSpaces = spaces.findAllByMapId(mapId);

//        if(findAllSpaces.isEmpty()) {
//            findAllSpaces = spaces.findAll().stream()
//                    .map(space -> Space.of(space, Collections.emptyList()))
//                    .collect(Collectors.toList());
//        }

        return SpaceFindAllResponse.from(findAllSpaces);
    }

    public void updateSpace(
            final Long mapId,
            final Long spaceId,
            final SpaceCreateUpdateRequest spaceCreateUpdateRequest,
            final Member manager) {
        Map map = maps.findById(mapId)
                .orElseThrow(NoSuchMapException::new);
        validateAuthorityOnMap(manager, map);

        Space space = spaces.findById(spaceId).orElseThrow(NoSuchSpaceException::new);
//                .orElse(Space.of(spaces.findById(spaceId).orElseThrow(NoSuchSpaceException::new), Collections.emptyList()));
        Space updateSpace = getUpdateSpace(spaceCreateUpdateRequest, map);

        space.update(updateSpace);
    }

    public void deleteSpace(
            final Long mapId,
            final Long spaceId,
            final Member manager) {
        Map map = maps.findById(mapId)
                .orElseThrow(NoSuchMapException::new);
        validateAuthorityOnMap(manager, map);

        Space space = spaces.findById(spaceId).orElseThrow(NoSuchSpaceException::new);
//                .orElse(Space.of(spaces.findById(spaceId).orElseThrow(NoSuchSpaceException::new), Collections.emptyList()));

        validateReservationExistence(space);
//        validateReservationExistence(spaceId);

        spaces.delete(space);
    }

    private Space getUpdateSpace(
            final SpaceCreateUpdateRequest spaceCreateUpdateRequest,
            final Map map) {
        SettingsRequest settingsRequest = spaceCreateUpdateRequest.getSettingsRequest();

        Setting updateSetting = new Setting.Builder()
                .availableStartTime(settingsRequest.getAvailableStartTime())
                .availableEndTime(settingsRequest.getAvailableEndTime())
                .reservationTimeUnit(settingsRequest.getReservationTimeUnit())
                .reservationEnable(settingsRequest.getReservationEnable())
                .reservationMinimumTimeUnit(settingsRequest.getReservationMinimumTimeUnit())
                .reservationMaximumTimeUnit(settingsRequest.getReservationMaximumTimeUnit())
                .disabledWeekdays(settingsRequest.getDisabledWeekdays())
                .build();

        return new Space.Builder()
                .name(spaceCreateUpdateRequest.getSpaceName())
                .color(spaceCreateUpdateRequest.getColor())
                .description(spaceCreateUpdateRequest.getDescription())
                .area(spaceCreateUpdateRequest.getArea())
                .setting(updateSetting)
                .map(map)
                .build();
    }

    private void validateReservationExistence(final Space space){
        if(!space.getReservations().isEmpty()) {
        //        if (reservations.existsBySpaceIdAndEndTimeAfter(spaceId, timeConverter.getNow())) {
            throw new ReservationExistOnSpaceException();
        }
    }

    private void validateAuthorityOnMap(final Member manager, final Map map) {
        if (map.isNotOwnedBy(manager)) {
            throw new NoAuthorityOnMapException();
        }
    }
}
