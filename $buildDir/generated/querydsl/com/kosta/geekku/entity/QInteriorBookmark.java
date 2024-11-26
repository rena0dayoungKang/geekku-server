package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInteriorBookmark is a Querydsl query type for InteriorBookmark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInteriorBookmark extends EntityPathBase<InteriorBookmark> {

    private static final long serialVersionUID = -2022037360L;

    public static final QInteriorBookmark interiorBookmark = new QInteriorBookmark("interiorBookmark");

    public final NumberPath<Integer> bookmarkInteriorNum = createNumber("bookmarkInteriorNum", Integer.class);

    public final NumberPath<Integer> interiorNum = createNumber("interiorNum", Integer.class);

    public final ComparablePath<java.util.UUID> userId = createComparable("userId", java.util.UUID.class);

    public QInteriorBookmark(String variable) {
        super(InteriorBookmark.class, forVariable(variable));
    }

    public QInteriorBookmark(Path<? extends InteriorBookmark> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInteriorBookmark(PathMetadata metadata) {
        super(InteriorBookmark.class, metadata);
    }

}

