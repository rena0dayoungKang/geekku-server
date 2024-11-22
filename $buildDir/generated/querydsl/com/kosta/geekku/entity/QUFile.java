package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUFile is a Querydsl query type for UFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUFile extends EntityPathBase<UFile> {

    private static final long serialVersionUID = 1265737799L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUFile uFile = new QUFile("uFile");

    public final QCompany company;

    public final StringPath contentType = createString("contentType");

    public final StringPath directory = createString("directory");

    public final StringPath name = createString("name");

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final DatePath<java.sql.Date> uploadDate = createDate("uploadDate", java.sql.Date.class);

    public final NumberPath<Integer> userImageNum = createNumber("userImageNum", Integer.class);

    public QUFile(String variable) {
        this(UFile.class, forVariable(variable), INITS);
    }

    public QUFile(Path<? extends UFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUFile(PathMetadata metadata, PathInits inits) {
        this(UFile.class, metadata, inits);
    }

    public QUFile(Class<? extends UFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
    }

}

