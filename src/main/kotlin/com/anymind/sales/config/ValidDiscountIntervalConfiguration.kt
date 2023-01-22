package com.anymind.sales.config

import com.anymind.sales.entity.PaymentMethod
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app.discount")
class ValidDiscountIntervalConfiguration(
    val modifier: Map<PaymentMethod, IntervalConfig>
) {
    data class IntervalConfig(
        val fromInclusive: Double,
        val toInclusive: Double
    )
}