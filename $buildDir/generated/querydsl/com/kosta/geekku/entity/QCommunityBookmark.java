package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunityBookmark is a Querydsl query type for CommunityBookmark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityBookmark extends EntityPathBase<CommunityBookmark> {

    private static final long serialVersionUID = -77375499L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunityBookmark communityBookmark = new QCommunityBookmark("communityBookmark");

    public final NumberPath<Integer> bookmarkCommunityNum = createNumber("bookmarkCommunityNum", Integer.class);

    public final QCommunity community;

    public final QUser user;

    public QCommunityBookmark(String variable) {
        this(CommunityBookmark.class, forVariable(variable), INITS);
    }

    public QCommunityBookmark(Path<? extends CommunityBookmark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunityBookmark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunityBookmark(PathMetadata metadata, PathInits inits) {
        this(CommunityBookmark.class, metadata, inits);
    }

    public QCommunityBookmark(Class<? extends CommunityBookmark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.community = inits.isInitialized("community") ? new QCommunity(forProperty("community"), inits.get("community")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

