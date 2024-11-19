package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEstateImage is a Querydsl query type for EstateImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEstateImage extends EntityPathBase<EstateImage> {

    private static final long serialVersionUID = 411321765L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEstateImage estateImage = new QEstateImage("estateImage");

    public final StringPath contentType = createString("contentType");

    public final StringPath directory = createString("directory");

    public final QEstate estate;

    public final NumberPath<Integer> estateImageNum = createNumber("estateImageNum", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final DatePath<java.sql.Date> uploadDate = createDate("uploadDate", java.sql.Date.class);

    public QEstateImage(String variable) {
        this(EstateImage.class, forVariable(variable), INITS);
    }

    public QEstateImage(Path<? extends EstateImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEstateImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEstateImage(PathMetadata metadata, PathInits inits) {
        this(EstateImage.class, metadata, inits);
    }

    public QEstateImage(Class<? extends EstateImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.estate = inits.isInitialized("estate") ? new QEstate(forProperty("estate"), inits.get("estate")) : null;
    }

}

