package com.woowacourse.zzimkkong;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface PerformanceLogging {
}
