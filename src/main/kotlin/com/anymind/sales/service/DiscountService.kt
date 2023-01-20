package com.anymind.sales.service

import com.anymind.sales.config.ValidDiscountIntervalConfiguration
import com.anymind.sales.entity.PaymentMethod
import com.anymind.sales.exception.BadRequestException
import org.springframework.stereotype.Service

@Service
class DiscountService(
    private val discountConfiguration: ValidDiscountIntervalConfiguration
) {
    fun validatePriceModifierForPaymentMethod(priceModifier: Double, paymentMethod: PaymentMethod) {
        val validDiscountInterval = discountConfiguration.modifier[paymentMethod]
            ?: throw IllegalArgumentException("paymentMethod=$paymentMethod is not mapped to a discount interval.")
        if (priceModifier !in validDiscountInterval.fromInclusive..validDiscountInterval.toInclusive) {
            throw BadRequestException("priceModifier=$priceModifier doesnt fit paymentMethod=$paymentMethod requirements!")
        }
    }

    data class ValidDiscountIntervalConfig(
        val fromInclusive: Double,
        val toInclusive: Double
    )
}
