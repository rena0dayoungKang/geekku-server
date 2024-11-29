package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInteriorAllRequest is a Querydsl query type for InteriorAllRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInteriorAllRequest extends EntityPathBase<InteriorAllRequest> {

    private static final long serialVersionUID = 1905802888L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInteriorAllRequest interiorAllRequest = new QInteriorAllRequest("interiorAllRequest");

    public final StringPath addContent = createString("addContent");

    public final StringPath address1 = createString("address1");

    public final StringPath address2 = createString("address2");

    public final BooleanPath allowPhone = createBoolean("allowPhone");

    public final DateTimePath<java.sql.Timestamp> createAt = createDateTime("createAt", java.sql.Timestamp.class);

    public final StringPath interiorType = createString("interiorType");

    public final NumberPath<Integer> money = createNumber("money", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public final NumberPath<Integer> requestAllNum = createNumber("requestAllNum", Integer.class);

    public final NumberPath<Integer> size = createNumber("size", Integer.class);

    public final StringPath title = createString("title");

    public final StringPath type = createString("type");

    public final QUser user;

    public final BooleanPath workType = createBoolean("workType");
    
    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QInteriorAllRequest(String variable) {
        this(InteriorAllRequest.class, forVariable(variable), INITS);
    }

    public QInteriorAllRequest(Path<? extends InteriorAllRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInteriorAllRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInteriorAllRequest(PathMetadata metadata, PathInits inits) {
        this(InteriorAllRequest.class, metadata, inits);
    }

    public QInteriorAllRequest(Class<? extends InteriorAllRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

