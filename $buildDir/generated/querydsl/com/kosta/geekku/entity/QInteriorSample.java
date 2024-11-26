package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInteriorSample is a Querydsl query type for InteriorSample
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInteriorSample extends EntityPathBase<InteriorSample> {

    private static final long serialVersionUID = -11073660L;

    public static final QInteriorSample interiorSample = new QInteriorSample("interiorSample");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> coverImage = createNumber("coverImage", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final NumberPath<Integer> interiorNum = createNumber("interiorNum", Integer.class);

    public final QInterior interior;

    public final StringPath intro = createString("intro");

    public final StringPath location = createString("location");

    public final NumberPath<Integer> sampleNum = createNumber("sampleNum", Integer.class);

    public final NumberPath<Integer> size = createNumber("size", Integer.class);

    public final StringPath style = createString("style");

    public final StringPath type = createString("type");

    public QInteriorSample(String variable) {
        super(InteriorSample.class, forVariable(variable));
    }

    public QInteriorSample(Path<? extends InteriorSample> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInteriorSample(PathMetadata metadata) {
        super(InteriorSample.class, metadata);
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInteriorSample(Class<? extends InteriorSample> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
        this.interior = inits.isInitialized("interior") ? new QInterior(forProperty("interior"), inits.get("interior")) : null;
    }

}

