package com.woowacourse.zzimkkong.dto.reservation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.woowacourse.zzimkkong.dto.ValidatorMessage.*;

public class ReservationPasswordAuthenticationRequest {
    @NotBlank(message = EMPTY_MESSAGE)
    @Pattern(regexp = RESERVATION_PASSWORD_FORMAT, message = RESERVATION_PASSWORD_MESSAGE)
    private String password;

    public ReservationPasswordAuthenticationRequest() {
    }

    public ReservationPasswordAuthenticationRequest(final String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
