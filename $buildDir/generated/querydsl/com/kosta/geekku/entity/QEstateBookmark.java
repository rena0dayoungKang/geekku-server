package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QEstateBookmark is a Querydsl query type for EstateBookmark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEstateBookmark extends EntityPathBase<EstateBookmark> {

    private static final long serialVersionUID = -1284887380L;

    public static final QEstateBookmark estateBookmark = new QEstateBookmark("estateBookmark");

    public final NumberPath<Integer> bookmarkEstateNum = createNumber("bookmarkEstateNum", Integer.class);

    public final NumberPath<Integer> estateNum = createNumber("estateNum", Integer.class);

    public final ComparablePath<java.util.UUID> userId = createComparable("userId", java.util.UUID.class);

    public QEstateBookmark(String variable) {
        super(EstateBookmark.class, forVariable(variable));
    }

    public QEstateBookmark(Path<? extends EstateBookmark> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEstateBookmark(PathMetadata metadata) {
        super(EstateBookmark.class, metadata);
    }

}

