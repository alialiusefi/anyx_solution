package com.anymind.sales.service

import com.anymind.sales.config.PointMultiplierConfiguration
import com.anymind.sales.entity.PaymentMethod
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class PointService(
    private val paymentMethodPointMultiplierMap: PointMultiplierConfiguration
) {
    fun getPointMultiplier(paymentMethod: PaymentMethod): Double =
        paymentMethodPointMultiplierMap.multiplier[paymentMethod]
            ?: throw IllegalArgumentException("paymentMethod=$paymentMethod is not mapped to a point multiplier.")

    fun calculatePoints(price: BigDecimal, paymentMethod: PaymentMethod): Long {
        val multiplier = BigDecimal(getPointMultiplier(paymentMethod))
        val pointsBigDecimal = price.multiply(multiplier)
        return pointsBigDecimal.toLong()
    }
}