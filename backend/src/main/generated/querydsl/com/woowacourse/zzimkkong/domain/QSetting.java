package com.woowacourse.zzimkkong.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QSetting is a Querydsl query type for Setting
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QSetting extends BeanPath<Setting> {

    private static final long serialVersionUID = 423672847L;

    public static final QSetting setting = new QSetting("setting");

    public final TimePath<java.time.LocalTime> availableEndTime = createTime("availableEndTime", java.time.LocalTime.class);

    public final TimePath<java.time.LocalTime> availableStartTime = createTime("availableStartTime", java.time.LocalTime.class);

    public final StringPath disabledWeekdays = createString("disabledWeekdays");

    public final BooleanPath reservationEnable = createBoolean("reservationEnable");

    public final NumberPath<Integer> reservationMaximumTimeUnit = createNumber("reservationMaximumTimeUnit", Integer.class);

    public final NumberPath<Integer> reservationMinimumTimeUnit = createNumber("reservationMinimumTimeUnit", Integer.class);

    public final NumberPath<Integer> reservationTimeUnit = createNumber("reservationTimeUnit", Integer.class);

    public QSetting(String variable) {
        super(Setting.class, forVariable(variable));
    }

    public QSetting(Path<? extends Setting> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSetting(PathMetadata metadata) {
        super(Setting.class, metadata);
    }

}

