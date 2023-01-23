package com.anymind.sales.config

import com.anymind.sales.entity.PaymentMethod
import mu.KLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app.point")
class PointMultiplierConfiguration(
    val multiplier: Map<PaymentMethod, Double>
) {
    companion object : KLogging()

    init {
        logger.debug { "Point multipliers configured with the following configuration: $multiplier" }
    }
}
