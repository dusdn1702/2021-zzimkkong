package com.woowacourse.zzimkkong.dto;

import com.woowacourse.zzimkkong.dto.reservation.ReservationPasswordAuthenticationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.woowacourse.zzimkkong.dto.ValidatorMessage.EMPTY_MESSAGE;
import static com.woowacourse.zzimkkong.dto.ValidatorMessage.RESERVATION_PASSWORD_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

class ReservationPasswordAuthenticationRequestTest extends RequestTest {
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("예약 비밀번호에 빈 문자열이 들어오면 처리한다.")
    public void blankReservationPassword(String password) {
        ReservationPasswordAuthenticationRequest reservationPasswordAuthenticationRequest = new ReservationPasswordAuthenticationRequest(password);

        assertThat(getConstraintViolations(reservationPasswordAuthenticationRequest).stream()
                .anyMatch(violation -> violation.getMessage().equals(EMPTY_MESSAGE)))
                .isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"12341:true", "123:true", "123!:true", "1234:false"}, delimiter = ':')
    @DisplayName("예약 비밀번호에 옳지 않은 형식의 비밀번호가 들어오면 처리한다.")
    public void invalidEmail(String password, boolean flag) {
        ReservationPasswordAuthenticationRequest reservationPasswordAuthenticationRequest = new ReservationPasswordAuthenticationRequest(password);

        assertThat(getConstraintViolations(reservationPasswordAuthenticationRequest).stream()
                .anyMatch(violation -> violation.getMessage().equals(RESERVATION_PASSWORD_MESSAGE)))
                .isEqualTo(flag);
    }
}
