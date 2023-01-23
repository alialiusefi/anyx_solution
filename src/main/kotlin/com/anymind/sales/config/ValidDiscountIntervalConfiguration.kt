package com.anymind.sales.config

import com.anymind.sales.entity.PaymentMethod
import mu.KLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app.discount")
class ValidDiscountIntervalConfiguration(
    val modifier: Map<PaymentMethod, IntervalConfig>
) {
    companion object : KLogging()

    init {
        logger.debug { "Acceptable price modifier intervals configured with the following configuration: $modifier" }
    }

    data class IntervalConfig(
        val fromInclusive: Double,
        val toInclusive: Double
    )
}
