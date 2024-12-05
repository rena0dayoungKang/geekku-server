package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOnestop is a Querydsl query type for Onestop
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOnestop extends EntityPathBase<Onestop> {

    private static final long serialVersionUID = 1009923742L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOnestop onestop = new QOnestop("onestop");

    public final StringPath address1 = createString("address1");

    public final StringPath address2 = createString("address2");

    public final BooleanPath allowPhone = createBoolean("allowPhone");

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final StringPath interiorType = createString("interiorType");

    public final NumberPath<Integer> money = createNumber("money", Integer.class);

    public final NumberPath<Integer> movePersons = createNumber("movePersons", Integer.class);

    public final ListPath<OnestopAnswer, QOnestopAnswer> onestopAnswerList = this.<OnestopAnswer, QOnestopAnswer>createList("onestopAnswerList", OnestopAnswer.class, QOnestopAnswer.class, PathInits.DIRECT2);

    public final NumberPath<Integer> onestopNum = createNumber("onestopNum", Integer.class);

    public final StringPath rentType = createString("rentType");

    public final NumberPath<Integer> size = createNumber("size", Integer.class);

    public final StringPath title = createString("title");

    public final StringPath type = createString("type");

    public final QUser user;

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public final BooleanPath workType = createBoolean("workType");

    public QOnestop(String variable) {
        this(Onestop.class, forVariable(variable), INITS);
    }

    public QOnestop(Path<? extends Onestop> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOnestop(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOnestop(PathMetadata metadata, PathInits inits) {
        this(Onestop.class, metadata, inits);
    }

    public QOnestop(Class<? extends Onestop> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

