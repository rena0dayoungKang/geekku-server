package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCFile is a Querydsl query type for CFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCFile extends EntityPathBase<CFile> {

    private static final long serialVersionUID = 1249114421L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCFile cFile = new QCFile("cFile");

    public final QCommunity community;

    public final StringPath contentType = createString("contentType");

    public final StringPath directory = createString("directory");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public QCFile(String variable) {
        this(CFile.class, forVariable(variable), INITS);
    }

    public QCFile(Path<? extends CFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCFile(PathMetadata metadata, PathInits inits) {
        this(CFile.class, metadata, inits);
    }

    public QCFile(Class<? extends CFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.community = inits.isInitialized("community") ? new QCommunity(forProperty("community"), inits.get("community")) : null;
    }

}

