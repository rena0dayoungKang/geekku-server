package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEstateBookmark is a Querydsl query type for EstateBookmark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEstateBookmark extends EntityPathBase<EstateBookmark> {

    private static final long serialVersionUID = -1284887380L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEstateBookmark estateBookmark = new QEstateBookmark("estateBookmark");

    public final NumberPath<Integer> bookmarkEstateNum = createNumber("bookmarkEstateNum", Integer.class);

    public final QEstate estate;

    public final ComparablePath<java.util.UUID> userId = createComparable("userId", java.util.UUID.class);

    public QEstateBookmark(String variable) {
        this(EstateBookmark.class, forVariable(variable), INITS);
    }

    public QEstateBookmark(Path<? extends EstateBookmark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEstateBookmark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEstateBookmark(PathMetadata metadata, PathInits inits) {
        this(EstateBookmark.class, metadata, inits);
    }

    public QEstateBookmark(Class<? extends EstateBookmark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.estate = inits.isInitialized("estate") ? new QEstate(forProperty("estate"), inits.get("estate")) : null;
    }

}

