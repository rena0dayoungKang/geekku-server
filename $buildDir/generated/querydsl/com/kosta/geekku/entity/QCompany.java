package com.kosta.geekku.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = -1014276173L;

    public static final QCompany company = new QCompany("company");

    public final StringPath address = createString("address");

    public final StringPath ceoName = createString("ceoName");

    public final StringPath companyAddress = createString("companyAddress");

    public final StringPath companyCertificationImage = createString("companyCertificationImage");

    public final ComparablePath<java.util.UUID> companyId = createComparable("companyId", java.util.UUID.class);

    public final StringPath companyName = createString("companyName");

    public final StringPath companyNumber = createString("companyNumber");

    public final DateTimePath<java.sql.Timestamp> createdAt = createDateTime("createdAt", java.sql.Timestamp.class);

    public final StringPath email1 = createString("email1");

    public final StringPath email2 = createString("email2");

    public final StringPath estateNumber = createString("estateNumber");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final ArrayPath<byte[], Byte> profileImage = createArray("profileImage", byte[].class);

    public final BooleanPath status = createBoolean("status");

    public final StringPath type = createString("type");

    public final StringPath username = createString("username");

    public QCompany(String variable) {
        super(Company.class, forVariable(variable));
    }

    public QCompany(Path<? extends Company> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompany(PathMetadata metadata) {
        super(Company.class, metadata);
    }

}

