package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHouseAnswer is a Querydsl query type for HouseAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHouseAnswer extends EntityPathBase<HouseAnswer> {

    private static final long serialVersionUID = -1824083148L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHouseAnswer houseAnswer = new QHouseAnswer("houseAnswer");

    public final NumberPath<Integer> answerHouseNum = createNumber("answerHouseNum", Integer.class);

    public final QCompany company;

    public final StringPath content = createString("content");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final QHouse house;

    public QHouseAnswer(String variable) {
        this(HouseAnswer.class, forVariable(variable), INITS);
    }

    public QHouseAnswer(Path<? extends HouseAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHouseAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHouseAnswer(PathMetadata metadata, PathInits inits) {
        this(HouseAnswer.class, metadata, inits);
    }

    public QHouseAnswer(Class<? extends HouseAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
        this.house = inits.isInitialized("house") ? new QHouse(forProperty("house"), inits.get("house")) : null;
    }

}

