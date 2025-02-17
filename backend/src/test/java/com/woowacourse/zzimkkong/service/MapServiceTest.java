package com.woowacourse.zzimkkong.service;

import com.woowacourse.zzimkkong.domain.Map;
import com.woowacourse.zzimkkong.domain.Member;
import com.woowacourse.zzimkkong.domain.Setting;
import com.woowacourse.zzimkkong.domain.Space;
import com.woowacourse.zzimkkong.dto.map.MapCreateResponse;
import com.woowacourse.zzimkkong.dto.map.MapCreateUpdateRequest;
import com.woowacourse.zzimkkong.dto.map.MapFindAllResponse;
import com.woowacourse.zzimkkong.dto.map.MapFindResponse;
import com.woowacourse.zzimkkong.exception.authorization.NoAuthorityOnMapException;
import com.woowacourse.zzimkkong.exception.space.ReservationExistOnSpaceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.woowacourse.zzimkkong.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

class MapServiceTest extends ServiceTest {
    @Autowired
    private MapService mapService;
    private Member pobi;
    private Map luther;
    private Map smallHouse;
    private Space be;
    private Space fe;

    @BeforeEach
    void setUp() {
        pobi = new Member(EMAIL, PASSWORD, ORGANIZATION);
        luther = new Map(1L, LUTHER_NAME, MAP_DRAWING_DATA, MAP_IMAGE_URL, pobi);
        smallHouse = new Map(2L, SMALL_HOUSE_NAME, MAP_DRAWING_DATA, MAP_IMAGE_URL, pobi);

        Setting beSetting = new Setting.Builder()
                .availableStartTime(BE_AVAILABLE_START_TIME)
                .availableEndTime(BE_AVAILABLE_END_TIME)
                .reservationTimeUnit(BE_RESERVATION_TIME_UNIT)
                .reservationMinimumTimeUnit(BE_RESERVATION_MINIMUM_TIME_UNIT)
                .reservationMaximumTimeUnit(BE_RESERVATION_MAXIMUM_TIME_UNIT)
                .reservationEnable(BE_RESERVATION_ENABLE)
                .enabledDayOfWeek(BE_ENABLED_DAY_OF_WEEK)
                .build();

        be = new Space.Builder()
                .id(1L)
                .name(BE_NAME)
                .map(luther)
                .description(BE_DESCRIPTION)
                .area(SPACE_DRAWING)
                .setting(beSetting)
                .build();

        Setting feSetting = new Setting.Builder()
                .availableStartTime(FE_AVAILABLE_START_TIME)
                .availableEndTime(FE_AVAILABLE_END_TIME)
                .reservationTimeUnit(FE_RESERVATION_TIME_UNIT)
                .reservationMinimumTimeUnit(FE_RESERVATION_MINIMUM_TIME_UNIT)
                .reservationMaximumTimeUnit(FE_RESERVATION_MAXIMUM_TIME_UNIT)
                .reservationEnable(FE_RESERVATION_ENABLE)
                .enabledDayOfWeek(FE_ENABLED_DAY_OF_WEEK)
                .build();

        fe = new Space.Builder()
                .id(2L)
                .name(FE_NAME)
                .color(FE_COLOR)
                .map(luther)
                .description(FE_DESCRIPTION)
                .area(SPACE_DRAWING)
                .setting(feSetting)
                .build();
    }

    @Test
    @DisplayName("맵 생성 요청 시, 올바른 요청이 들어오면 맵을 생성한다.")
    void create() {
        //given
        MapCreateUpdateRequest mapCreateUpdateRequest = new MapCreateUpdateRequest(luther.getName(), luther.getMapDrawing(), MAP_SVG);

        //when
        given(maps.save(any(Map.class)))
                .willReturn(luther);
        given(storageUploader.upload(anyString(), any(File.class)))
                .willReturn(MAP_IMAGE_URL);

        //then
        MapCreateResponse mapCreateResponse = mapService.saveMap(mapCreateUpdateRequest, pobi);
        assertThat(mapCreateResponse.getId()).isEqualTo(luther.getId());
    }

    @Test
    @DisplayName("맵 조회 요청 시, mapId에 해당하는 맵을 조회한다.")
    void find() {
        //given
        given(maps.findById(anyLong()))
                .willReturn(Optional.of(luther));

        //when
        MapFindResponse mapFindResponse = mapService.findMap(luther.getId(), pobi);

        //then
        assertThat(mapFindResponse).usingRecursiveComparison()
                .isEqualTo(MapFindResponse.from(luther));
    }

    @Test
    @DisplayName("모든 맵 조회 요청 시, member가 가진 모든 맵을 조회한다.")
    void findAll() {
        //given
        List<Map> expectedMaps = List.of(luther, smallHouse);
        given(maps.findAllByMember(any(Member.class)))
                .willReturn(expectedMaps);

        //when
        MapFindAllResponse mapFindAllResponse = mapService.findAllMaps(pobi);

        //then
        assertThat(mapFindAllResponse).usingRecursiveComparison()
                .isEqualTo(MapFindAllResponse.of(expectedMaps, pobi));
    }

    @Test
    @DisplayName("맵 수정 요청이 들어오면 수정한다.")
    void update() {
        //given
        MapCreateUpdateRequest mapCreateUpdateRequest = new MapCreateUpdateRequest("이름을 바꿔요", luther.getMapDrawing(), MAP_SVG);
        given(maps.findById(anyLong()))
                .willReturn(Optional.of(luther));
        given(storageUploader.upload(anyString(), any(File.class)))
                .willReturn(MAP_IMAGE_URL);

        //when, then
        assertDoesNotThrow(() -> mapService.updateMap(luther.getId(), mapCreateUpdateRequest, pobi));
    }

    @Test
    @DisplayName("권한이 없는 관리자가 맵을 수정하려고 할 경우 예외가 발생한다.")
    void updateManagerException() {
        //given
        Member anotherMember = new Member("sally@email.com", "password", "organization");
        MapCreateUpdateRequest mapCreateUpdateRequest = new MapCreateUpdateRequest("이름을 바꿔요", luther.getMapDrawing(), MAP_SVG);

        given(maps.findById(anyLong()))
                .willReturn(Optional.of(luther));

        // when, then
        assertThatThrownBy(() -> mapService.updateMap(luther.getId(), mapCreateUpdateRequest, anotherMember))
                .isInstanceOf(NoAuthorityOnMapException.class);
    }

    @Test
    @DisplayName("맵 삭제 요청 시, 이후에 존재하는 예약이 없다면 삭제한다.")
    void delete() {
        //given
        given(maps.findById(anyLong()))
                .willReturn(Optional.of(luther));

        given(spaces.findAllByMapId(anyLong()))
                .willReturn(List.of(be, fe));

        given(reservations.existsBySpaceIdAndEndTimeAfter(anyLong(), any(LocalDateTime.class)))
                .willReturn(false);

        //when, then
        assertDoesNotThrow(() -> mapService.deleteMap(luther.getId(), pobi));
    }

    @Test
    @DisplayName("맵 삭제 요청 시, 이후에 존재하는 예약이 있다면 예외가 발생한다.")
    void deleteExistReservationException() {
        //given
        given(maps.findById(anyLong()))
                .willReturn(Optional.of(luther));

        given(spaces.findAllByMapId(anyLong()))
                .willReturn(List.of(be, fe));

        given(reservations.existsBySpaceIdAndEndTimeAfter(anyLong(), any(LocalDateTime.class)))
                .willReturn(true);

        //when, then
        assertThatThrownBy(() -> mapService.deleteMap(luther.getId(), pobi))
                .isInstanceOf(ReservationExistOnSpaceException.class);
    }
}
