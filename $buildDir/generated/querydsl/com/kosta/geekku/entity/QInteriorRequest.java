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

    public final StringPath allowTime = createString("allowTime");

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final QInterior interior;

    public final StringPath name = createString("name");

    public final StringPath period = createString("period");

    public final StringPath phone = createString("phone");

    public final NumberPath<Integer> requestNum = createNumber("requestNum", Integer.class);

    public final StringPath size = createString("size");

    public final StringPath status = createString("status");

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
        this.interior = inits.isInitialized("interior") ? new QInterior(forProperty("interior"), inits.get("interior")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

