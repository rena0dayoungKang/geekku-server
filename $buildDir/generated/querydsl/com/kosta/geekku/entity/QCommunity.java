package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunity is a Querydsl query type for Community
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunity extends EntityPathBase<Community> {

    private static final long serialVersionUID = 170745247L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunity community = new QCommunity("community");

    public final StringPath address1 = createString("address1");

    public final StringPath address2 = createString("address2");

    public final ListPath<CommunityComment, QCommunityComment> commentList = this.<CommunityComment, QCommunityComment>createList("commentList", CommunityComment.class, QCommunityComment.class, PathInits.DIRECT2);

    public final ListPath<CommunityBookmark, QCommunityBookmark> communityBookmarkList = this.<CommunityBookmark, QCommunityBookmark>createList("communityBookmarkList", CommunityBookmark.class, QCommunityBookmark.class, PathInits.DIRECT2);

    public final NumberPath<Integer> communityNum = createNumber("communityNum", Integer.class);

    public final StringPath content = createString("content");

    public final StringPath coverImage = createString("coverImage");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final StringPath familyType = createString("familyType");

    public final StringPath interiorType = createString("interiorType");

    public final NumberPath<Integer> money = createNumber("money", Integer.class);

    public final DatePath<java.sql.Date> periodEndDate = createDate("periodEndDate", java.sql.Date.class);

    public final DatePath<java.sql.Date> periodStartDate = createDate("periodStartDate", java.sql.Date.class);

    public final NumberPath<Integer> size = createNumber("size", Integer.class);

    public final StringPath style = createString("style");

    public final StringPath title = createString("title");

    public final StringPath type = createString("type");

    public final QUser user;

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QCommunity(String variable) {
        this(Community.class, forVariable(variable), INITS);
    }

    public QCommunity(Path<? extends Community> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunity(PathMetadata metadata, PathInits inits) {
        this(Community.class, metadata, inits);
    }

    public QCommunity(Class<? extends Community> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

