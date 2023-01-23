package com.anymind.sales.entity

import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
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
    val datetime: Timestamp
)
