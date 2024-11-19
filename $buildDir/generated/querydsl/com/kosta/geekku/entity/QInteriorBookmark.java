package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInteriorBookmark is a Querydsl query type for InteriorBookmark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInteriorBookmark extends EntityPathBase<InteriorBookmark> {

    private static final long serialVersionUID = -2022037360L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInteriorBookmark interiorBookmark = new QInteriorBookmark("interiorBookmark");

    public final NumberPath<Integer> bookmarkInteriorNum = createNumber("bookmarkInteriorNum", Integer.class);

    public final QCompany company;

    public final QUser user;

    public QInteriorBookmark(String variable) {
        this(InteriorBookmark.class, forVariable(variable), INITS);
    }

    public QInteriorBookmark(Path<? extends InteriorBookmark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInteriorBookmark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInteriorBookmark(PathMetadata metadata, PathInits inits) {
        this(InteriorBookmark.class, metadata, inits);
    }

    public QInteriorBookmark(Class<? extends InteriorBookmark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

