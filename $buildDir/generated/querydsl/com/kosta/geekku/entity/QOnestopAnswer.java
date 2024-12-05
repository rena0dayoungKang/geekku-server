package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOnestopAnswer is a Querydsl query type for OnestopAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOnestopAnswer extends EntityPathBase<OnestopAnswer> {

    private static final long serialVersionUID = 2054779900L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOnestopAnswer onestopAnswer = new QOnestopAnswer("onestopAnswer");

    public final NumberPath<Integer> answerOnestopNum = createNumber("answerOnestopNum", Integer.class);

    public final QCompany company;

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final QOnestop onestop;

    public final StringPath title = createString("title");

    public QOnestopAnswer(String variable) {
        this(OnestopAnswer.class, forVariable(variable), INITS);
    }

    public QOnestopAnswer(Path<? extends OnestopAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOnestopAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOnestopAnswer(PathMetadata metadata, PathInits inits) {
        this(OnestopAnswer.class, metadata, inits);
    }

    public QOnestopAnswer(Class<? extends OnestopAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
        this.onestop = inits.isInitialized("onestop") ? new QOnestop(forProperty("onestop"), inits.get("onestop")) : null;
    }

}

