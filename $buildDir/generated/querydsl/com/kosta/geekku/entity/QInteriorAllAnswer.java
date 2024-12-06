package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInteriorAllAnswer is a Querydsl query type for InteriorAllAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInteriorAllAnswer extends EntityPathBase<InteriorAllAnswer> {

    private static final long serialVersionUID = -1248128859L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInteriorAllAnswer interiorAllAnswer = new QInteriorAllAnswer("interiorAllAnswer");

    public final NumberPath<Integer> answerAllNum = createNumber("answerAllNum", Integer.class);

    public final QCompany company;

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final QInteriorAllRequest interiorAllRequest;

    public final StringPath title = createString("title");

    public QInteriorAllAnswer(String variable) {
        this(InteriorAllAnswer.class, forVariable(variable), INITS);
    }

    public QInteriorAllAnswer(Path<? extends InteriorAllAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInteriorAllAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInteriorAllAnswer(PathMetadata metadata, PathInits inits) {
        this(InteriorAllAnswer.class, metadata, inits);
    }

    public QInteriorAllAnswer(Class<? extends InteriorAllAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
        this.interiorAllRequest = inits.isInitialized("interiorAllRequest") ? new QInteriorAllRequest(forProperty("interiorAllRequest"), inits.get("interiorAllRequest")) : null;
    }

}

