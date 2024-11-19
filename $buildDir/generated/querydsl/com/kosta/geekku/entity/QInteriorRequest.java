package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInteriorRequest is a Querydsl query type for InteriorRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInteriorRequest extends EntityPathBase<InteriorRequest> {

    private static final long serialVersionUID = -1112433675L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInteriorRequest interiorRequest = new QInteriorRequest("interiorRequest");

    public final NumberPath<Integer> allowTime = createNumber("allowTime", Integer.class);

    public final QCompany company;

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> period = createNumber("period", Integer.class);

    public final StringPath phone = createString("phone");

    public final NumberPath<Integer> requestNum = createNumber("requestNum", Integer.class);

    public final NumberPath<Integer> size = createNumber("size", Integer.class);

    public final NumberPath<Integer> statue = createNumber("statue", Integer.class);

    public final StringPath type = createString("type");

    public final QUser user;

    public QInteriorRequest(String variable) {
        this(InteriorRequest.class, forVariable(variable), INITS);
    }

    public QInteriorRequest(Path<? extends InteriorRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInteriorRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInteriorRequest(PathMetadata metadata, PathInits inits) {
        this(InteriorRequest.class, metadata, inits);
    }

    public QInteriorRequest(Class<? extends InteriorRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

