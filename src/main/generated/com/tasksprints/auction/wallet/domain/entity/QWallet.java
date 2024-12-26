package com.tasksprints.auction.wallet.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWallet is a Querydsl query type for Wallet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWallet extends EntityPathBase<Wallet> {

    private static final long serialVersionUID = 2028529565L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWallet wallet = new QWallet("wallet");

    public final com.tasksprints.auction.common.entity.QBaseEntityWithUpdate _super = new com.tasksprints.auction.common.entity.QBaseEntityWithUpdate(this);

    public final NumberPath<java.math.BigDecimal> balance = createNumber("balance", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.tasksprints.auction.payment.domain.entity.Payment, com.tasksprints.auction.payment.domain.entity.QPayment> payments = this.<com.tasksprints.auction.payment.domain.entity.Payment, com.tasksprints.auction.payment.domain.entity.QPayment>createList("payments", com.tasksprints.auction.payment.domain.entity.Payment.class, com.tasksprints.auction.payment.domain.entity.QPayment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.tasksprints.auction.user.domain.entity.QUser user;

    public final StringPath userName = createString("userName");

    public QWallet(String variable) {
        this(Wallet.class, forVariable(variable), INITS);
    }

    public QWallet(Path<? extends Wallet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWallet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWallet(PathMetadata metadata, PathInits inits) {
        this(Wallet.class, metadata, inits);
    }

    public QWallet(Class<? extends Wallet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.tasksprints.auction.user.domain.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

