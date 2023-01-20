package com.anymind.sales.entity

import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "sales")
data class Sale(
    @Id
    val uuid: UUID,
    val price: BigDecimal,
    val priceModifier: Double,
    @Enumerated(EnumType.STRING)
    val paymentMethod: PaymentMethod,
    val pointMultiplier: Double,

    val datetime: LocalDateTime
)
