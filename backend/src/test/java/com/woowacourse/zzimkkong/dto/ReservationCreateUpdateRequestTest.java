package com.woowacourse.zzimkkong.dto;

import com.woowacourse.zzimkkong.dto.reservation.ReservationCreateUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static com.woowacourse.zzimkkong.dto.ValidatorMessage.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReservationCreateUpdateRequestTest extends RequestTest {
    @ParameterizedTest
    @NullSource
    @DisplayName("예약 생성에 빈 dateTime이 들어오면 처리한다.")
    public void blankDateTime(LocalDateTime dateTime) {
        ReservationCreateUpdateRequest startTime = new ReservationCreateUpdateRequest(
                dateTime,
                LocalDateTime.now(),
                "name",
                "description");

        ReservationCreateUpdateRequest endTime = new ReservationCreateUpdateRequest(
                LocalDateTime.now(),
                dateTime,
                "name",
                "description");

        assertThat(getConstraintViolations(startTime).stream()
                .anyMatch(violation -> violation.getMessage().equals(EMPTY_MESSAGE)))
                .isTrue();

        assertThat(getConstraintViolations(endTime).stream()
                .anyMatch(violation -> violation.getMessage().equals(EMPTY_MESSAGE)))
                .isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"2021-08-03T25:00:00", "2021/08/03T00:00:00", "2021-08-0300:00:00"})
    @DisplayName("예약 생성에 옳지 않은 형식의 dateTime이 들어오면 처리한다.")
    public void invalidDateTime(String dateTime) {
        assertThatThrownBy(() -> new ReservationCreateUpdateRequest(
                LocalDateTime.parse(dateTime),
                LocalDateTime.parse(dateTime),
                "name",
                "description")).isInstanceOf(DateTimeParseException.class);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("예약 생성에 빈 이름이 들어오면 처리한다.")
    public void blankName(String data) {
        ReservationCreateUpdateRequest nameRequest = new ReservationCreateUpdateRequest(
                LocalDateTime.now(),
                LocalDateTime.now(),
                data,
                "description");

        assertThat(getConstraintViolations(nameRequest).stream()
                .anyMatch(violation -> violation.getMessage().equals(EMPTY_MESSAGE)))
                .isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"hihellomorethantwenty:true", "hi~~~~:true", "hihello!:false"}, delimiter = ':')
    @DisplayName("예약 생성의 이름에 옳지 않은 형식의 문자열이 들어오면 처리한다.")
    public void invalidName(String name, boolean flag) {
        ReservationCreateUpdateRequest reservationCreateUpdateRequest = new ReservationCreateUpdateRequest(
                LocalDateTime.now(),
                LocalDateTime.now(),
                name,
                "description");

        assertThat(getConstraintViolations(reservationCreateUpdateRequest).stream()
                .anyMatch(violation -> violation.getMessage().equals(NAME_MESSAGE)))
                .isEqualTo(flag);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("예약 생성에 빈 이름이 들어오면 처리한다.")
    public void blankDescription(String description) {
        ReservationCreateUpdateRequest descriptionRequest = new ReservationCreateUpdateRequest(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "name",
                description);

        assertThat(getConstraintViolations(descriptionRequest).stream()
                .anyMatch(violation -> violation.getMessage().equals(EMPTY_MESSAGE)))
                .isTrue();
    }

    @Test
    @DisplayName("예약 생성의 이름에 옳지 않은 형식의 문자열이 들어오면 처리한다.")
    public void invalidDescription() {
        ReservationCreateUpdateRequest reservationCreateUpdateRequest = new ReservationCreateUpdateRequest(
                LocalDateTime.now(),
                LocalDateTime.now(),
                "name",
                "iamtenwordiamtenwordiamtenwordiamtenwordiamtenwordiamtenwordiamtenwordiamtenwordiamtenwordiamtenword1");

        assertThat(getConstraintViolations(reservationCreateUpdateRequest).stream()
                .anyMatch(violation -> violation.getMessage().equals(DESCRIPTION_MESSAGE)))
                .isEqualTo(true);
    }
}

