package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEstate is a Querydsl query type for Estate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEstate extends EntityPathBase<Estate> {

    private static final long serialVersionUID = 166975830L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEstate estate = new QEstate("estate");

    public final StringPath address1 = createString("address1");

    public final StringPath address2 = createString("address2");

    public final DatePath<java.sql.Date> availableDate = createDate("availableDate", java.sql.Date.class);

    public final BooleanPath availableState = createBoolean("availableState");

    public final NumberPath<Integer> bathCount = createNumber("bathCount", Integer.class);

    public final NumberPath<Integer> buyPrice = createNumber("buyPrice", Integer.class);

    public final QCompany company;

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final NumberPath<Integer> depositPrice = createNumber("depositPrice", Integer.class);

    public final NumberPath<Integer> estateNum = createNumber("estateNum", Integer.class);

    public final NumberPath<Integer> floor = createNumber("floor", Integer.class);

    public final ListPath<EstateImage, QEstateImage> imageList = this.<EstateImage, QEstateImage>createList("imageList", EstateImage.class, QEstateImage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> jeonsePrice = createNumber("jeonsePrice", Integer.class);

    public final StringPath jibunAddress = createString("jibunAddress");

    public final NumberPath<Integer> managePrice = createNumber("managePrice", Integer.class);

    public final NumberPath<Integer> monthlyPrice = createNumber("monthlyPrice", Integer.class);

    public final NumberPath<Integer> parking = createNumber("parking", Integer.class);

    public final StringPath rentType = createString("rentType");

    public final NumberPath<Integer> roomCount = createNumber("roomCount", Integer.class);

    public final StringPath size1 = createString("size1");

    public final StringPath size2 = createString("size2");

    public final StringPath title = createString("title");

    public final NumberPath<Integer> totalFloor = createNumber("totalFloor", Integer.class);

    public final StringPath type = createString("type");

    public final StringPath utility = createString("utility");

    public QEstate(String variable) {
        this(Estate.class, forVariable(variable), INITS);
    }

    public QEstate(Path<? extends Estate> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEstate(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEstate(PathMetadata metadata, PathInits inits) {
        this(Estate.class, metadata, inits);
    }

    public QEstate(Class<? extends Estate> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
    }

}

