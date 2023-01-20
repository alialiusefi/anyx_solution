package com.anymind.sales.config

import com.anymind.sales.entity.PaymentMethod
import com.anymind.sales.service.DiscountService
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component

@ConstructorBinding
@ConfigurationProperties(prefix = "app.discount")
class ValidDiscountIntervalConfiguration(
    val modifier: Map<PaymentMethod, DiscountService.ValidDiscountIntervalConfig>
)