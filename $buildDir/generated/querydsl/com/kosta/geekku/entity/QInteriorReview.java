package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInteriorReview is a Querydsl query type for InteriorReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInteriorReview extends EntityPathBase<InteriorReview> {

    private static final long serialVersionUID = -35747534L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInteriorReview interiorReview = new QInteriorReview("interiorReview");

    public final StringPath companyName = createString("companyName");

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final ListPath<InteriorReviewImage, QInteriorReviewImage> imagesList = this.<InteriorReviewImage, QInteriorReviewImage>createList("imagesList", InteriorReviewImage.class, QInteriorReviewImage.class, PathInits.DIRECT2);

    public final QInterior interior;

    public final StringPath location = createString("location");

    public final NumberPath<Integer> reviewNum = createNumber("reviewNum", Integer.class);

    public final NumberPath<Integer> size = createNumber("size", Integer.class);

    public final StringPath style = createString("style");

    public final StringPath type = createString("type");

    public final QUser user;

    public QInteriorReview(String variable) {
        this(InteriorReview.class, forVariable(variable), INITS);
    }

    public QInteriorReview(Path<? extends InteriorReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInteriorReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInteriorReview(PathMetadata metadata, PathInits inits) {
        this(InteriorReview.class, metadata, inits);
    }

    public QInteriorReview(Class<? extends InteriorReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.interior = inits.isInitialized("interior") ? new QInterior(forProperty("interior"), inits.get("interior")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

