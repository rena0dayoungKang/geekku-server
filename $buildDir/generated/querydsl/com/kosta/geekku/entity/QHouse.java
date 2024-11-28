package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHouse is a Querydsl query type for House
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHouse extends EntityPathBase<House> {

    private static final long serialVersionUID = 1254965206L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHouse house = new QHouse("house");

    public final StringPath address1 = createString("address1");

    public final StringPath address2 = createString("address2");

    public final BooleanPath allowPhone = createBoolean("allowPhone");

    public final NumberPath<Integer> buyPrice = createNumber("buyPrice", Integer.class);

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final NumberPath<Integer> depositPrice = createNumber("depositPrice", Integer.class);

    public final ListPath<HouseAnswer, QHouseAnswer> houseAnswerList = this.<HouseAnswer, QHouseAnswer>createList("houseAnswerList", HouseAnswer.class, QHouseAnswer.class, PathInits.DIRECT2);

    public final NumberPath<Integer> houseNum = createNumber("houseNum", Integer.class);

    public final NumberPath<Integer> jeonsePrice = createNumber("jeonsePrice", Integer.class);

    public final NumberPath<Integer> monthlyPrice = createNumber("monthlyPrice", Integer.class);

    public final StringPath rentType = createString("rentType");

    public final DatePath<java.sql.Date> requestDate = createDate("requestDate", java.sql.Date.class);

    public final BooleanPath requestState = createBoolean("requestState");

    public final NumberPath<Integer> size = createNumber("size", Integer.class);

    public final StringPath title = createString("title");

    public final StringPath type = createString("type");

    public final QUser user;

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QHouse(String variable) {
        this(House.class, forVariable(variable), INITS);
    }

    public QHouse(Path<? extends House> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHouse(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHouse(PathMetadata metadata, PathInits inits) {
        this(House.class, metadata, inits);
    }

    public QHouse(Class<? extends House> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

