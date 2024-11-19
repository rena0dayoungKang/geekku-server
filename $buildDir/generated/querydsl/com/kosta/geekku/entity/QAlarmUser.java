package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlarmUser is a Querydsl query type for AlarmUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmUser extends EntityPathBase<AlarmUser> {

    private static final long serialVersionUID = 927102834L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlarmUser alarmUser = new QAlarmUser("alarmUser");

    public final NumberPath<Integer> answerNum = createNumber("answerNum", Integer.class);

    public final QCompany company;

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final StringPath message = createString("message");

    public final NumberPath<Integer> requestNum = createNumber("requestNum", Integer.class);

    public final BooleanPath status = createBoolean("status");

    public final StringPath type = createString("type");

    public final QUser user;

    public final NumberPath<Integer> userAlarmNum = createNumber("userAlarmNum", Integer.class);

    public QAlarmUser(String variable) {
        this(AlarmUser.class, forVariable(variable), INITS);
    }

    public QAlarmUser(Path<? extends AlarmUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlarmUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlarmUser(PathMetadata metadata, PathInits inits) {
        this(AlarmUser.class, metadata, inits);
    }

    public QAlarmUser(Class<? extends AlarmUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

