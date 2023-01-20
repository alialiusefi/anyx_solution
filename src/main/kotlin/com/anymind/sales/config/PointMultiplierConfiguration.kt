package com.anymind.sales.config

import com.anymind.sales.entity.PaymentMethod
import com.anymind.sales.service.DiscountService
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@ConstructorBinding
@ConfigurationProperties(prefix = "app.point")
class PointMultiplierConfiguration(
    val multiplier: Map<PaymentMethod, Double>
)