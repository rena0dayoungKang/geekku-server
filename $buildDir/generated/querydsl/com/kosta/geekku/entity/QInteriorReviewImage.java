package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInteriorReviewImage is a Querydsl query type for InteriorReviewImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInteriorReviewImage extends EntityPathBase<InteriorReviewImage> {

    private static final long serialVersionUID = 509157193L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInteriorReviewImage interiorReviewImage = new QInteriorReviewImage("interiorReviewImage");

    public final StringPath contentType = createString("contentType");

    public final StringPath dierctory = createString("dierctory");

    public final QInteriorReview interiorReview;

    public final NumberPath<Integer> interiorReviewImageNum = createNumber("interiorReviewImageNum", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final DatePath<java.sql.Date> uploadDate = createDate("uploadDate", java.sql.Date.class);

    public QInteriorReviewImage(String variable) {
        this(InteriorReviewImage.class, forVariable(variable), INITS);
    }

    public QInteriorReviewImage(Path<? extends InteriorReviewImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInteriorReviewImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInteriorReviewImage(PathMetadata metadata, PathInits inits) {
        this(InteriorReviewImage.class, metadata, inits);
    }

    public QInteriorReviewImage(Class<? extends InteriorReviewImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.interiorReview = inits.isInitialized("interiorReview") ? new QInteriorReview(forProperty("interiorReview"), inits.get("interiorReview")) : null;
    }

}

