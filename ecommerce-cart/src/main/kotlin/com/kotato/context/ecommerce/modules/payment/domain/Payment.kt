package com.kotato.context.ecommerce.modules.payment.domain

import com.kotato.context.ecommerce.modules.payment.domain.create.PaymentCreatedEvent
import com.kotato.shared.money.Money
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import java.time.ZonedDateTime

@Aggregate
class Payment {

    @AggregateIdentifier
    lateinit var paymentId: PaymentId
        private set
    lateinit var price: Money
        private set
    lateinit var status: PaymentStatus
        private set

    @EventSourcingHandler
    fun on(event: PaymentCreatedEvent) {
        paymentId = PaymentId.fromString(event.aggregateId)
        price = Money.of(event.price, event.currency)
        status = PaymentStatus.PENDING
    }

    companion object {
        fun create(paymentId: PaymentId, amount: Money) {
            apply(PaymentCreatedEvent(paymentId.asString(),
                                      ZonedDateTime.now(),
                                      amount.amount,
                                      amount.currency))
        }
    }
}