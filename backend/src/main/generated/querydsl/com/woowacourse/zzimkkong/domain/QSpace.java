package com.woowacourse.zzimkkong.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpace is a Querydsl query type for Space
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSpace extends EntityPathBase<Space> {

    private static final long serialVersionUID = -785841531L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSpace space = new QSpace("space");

    public final StringPath area = createString("area");

    public final StringPath color = createString("color");

    public final StringPath coordinate = createString("coordinate");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMap map;

    public final StringPath name = createString("name");

    public final ListPath<Reservation, QReservation> reservations = this.<Reservation, QReservation>createList("reservations", Reservation.class, QReservation.class, PathInits.DIRECT2);

    public final QSetting setting;

    public final StringPath textPosition = createString("textPosition");

    public QSpace(String variable) {
        this(Space.class, forVariable(variable), INITS);
    }

    public QSpace(Path<? extends Space> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSpace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSpace(PathMetadata metadata, PathInits inits) {
        this(Space.class, metadata, inits);
    }

    public QSpace(Class<? extends Space> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.map = inits.isInitialized("map") ? new QMap(forProperty("map"), inits.get("map")) : null;
        this.setting = inits.isInitialized("setting") ? new QSetting(forProperty("setting")) : null;
    }

}

