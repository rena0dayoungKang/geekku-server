package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlarmInterior is a Querydsl query type for AlarmInterior
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmInterior extends EntityPathBase<AlarmInterior> {

    private static final long serialVersionUID = -1954605609L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlarmInterior alarmInterior = new QAlarmInterior("alarmInterior");

    public final QCompany company;

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final NumberPath<Integer> interiorAlarmNum = createNumber("interiorAlarmNum", Integer.class);

    public final StringPath message = createString("message");

    public final NumberPath<Integer> requestNum = createNumber("requestNum", Integer.class);

    public final BooleanPath status = createBoolean("status");

    public final QUser user;

    public QAlarmInterior(String variable) {
        this(AlarmInterior.class, forVariable(variable), INITS);
    }

    public QAlarmInterior(Path<? extends AlarmInterior> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlarmInterior(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlarmInterior(PathMetadata metadata, PathInits inits) {
        this(AlarmInterior.class, metadata, inits);
    }

    public QAlarmInterior(Class<? extends AlarmInterior> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

