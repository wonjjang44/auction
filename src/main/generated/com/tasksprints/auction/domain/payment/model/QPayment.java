package com.tasksprints.auction.domain.payment.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPayment is a Querydsl query type for Payment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPayment extends EntityPathBase<Payment> {

    private static final long serialVersionUID = -1228254623L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPayment payment = new QPayment("payment");

    public final com.tasksprints.auction.common.entity.QBaseEntityWithUpdate _super = new com.tasksprints.auction.common.entity.QBaseEntityWithUpdate(this);

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final StringPath cancelReason = createString("cancelReason");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath failReason = createString("failReason");

    public final StringPath orderName = createString("orderName");

    public final NumberPath<Long> paymentId = createNumber("paymentId", Long.class);

    public final EnumPath<PayStatus> payStatus = createEnum("payStatus", PayStatus.class);

    public final EnumPath<PayType> payType = createEnum("payType", PayType.class);

    public final StringPath tossOrderId = createString("tossOrderId");

    public final StringPath tossPaymentKey = createString("tossPaymentKey");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.tasksprints.auction.domain.wallet.model.QWallet wallet;

    public QPayment(String variable) {
        this(Payment.class, forVariable(variable), INITS);
    }

    public QPayment(Path<? extends Payment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPayment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPayment(PathMetadata metadata, PathInits inits) {
        this(Payment.class, metadata, inits);
    }

    public QPayment(Class<? extends Payment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.wallet = inits.isInitialized("wallet") ? new com.tasksprints.auction.domain.wallet.model.QWallet(forProperty("wallet"), inits.get("wallet")) : null;
    }

}

