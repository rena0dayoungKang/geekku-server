package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInterior is a Querydsl query type for Interior
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInterior extends EntityPathBase<Interior> {

    private static final long serialVersionUID = -207619782L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInterior interior = new QInterior("interior");

    public final QCompany company;

    public final StringPath content = createString("content");

    public final NumberPath<Integer> coverImage = createNumber("coverImage", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final NumberPath<Integer> InteriorNum = createNumber("InteriorNum", Integer.class);

    public final StringPath intro = createString("intro");

    public final NumberPath<Integer> period = createNumber("period", Integer.class);

    public final StringPath possibleLocation = createString("possibleLocation");

    public final BooleanPath possiblePart = createBoolean("possiblePart");

    public final NumberPath<Integer> recentCount = createNumber("recentCount", Integer.class);

    public final NumberPath<Integer> repairDate = createNumber("repairDate", Integer.class);

    public QInterior(String variable) {
        this(Interior.class, forVariable(variable), INITS);
    }

    public QInterior(Path<? extends Interior> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInterior(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInterior(PathMetadata metadata, PathInits inits) {
        this(Interior.class, metadata, inits);
    }

    public QInterior(Class<? extends Interior> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
    }

}

