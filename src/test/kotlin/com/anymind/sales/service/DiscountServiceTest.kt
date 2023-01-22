package com.anymind.sales.service

import com.anymind.sales.config.ValidDiscountIntervalConfiguration
import com.anymind.sales.entity.PaymentMethod
import com.anymind.sales.exception.BadRequestException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DiscountServiceTest {
    @Test
    fun shouldNotThrowExceptionWhenPassingValidArguments() {
        val config = ValidDiscountIntervalConfiguration(
            modifier = buildMap {
                put(
                    PaymentMethod.CASH,
                    ValidDiscountIntervalConfiguration.IntervalConfig(0.5, 0.9)
                )
            }
        )
        val service = DiscountService(config)

        service.validatePriceModifierForPaymentMethod(0.6, PaymentMethod.CASH)
    }

    @Test
    fun shouldThrowIllegalArgumentExceptionWhenMappedIncorrectly() {
        val config = ValidDiscountIntervalConfiguration(
            modifier = buildMap {}
        )
        val service = DiscountService(config)

        assertThrows<IllegalArgumentException> {
            service.validatePriceModifierForPaymentMethod(0.6, PaymentMethod.CASH)
        }
    }

    @Test
    fun shouldThrowBadRequestExceptionWhenPriceModifierDoesntFitInterval() {
        val config = ValidDiscountIntervalConfiguration(
            modifier = buildMap {
                put(
                    PaymentMethod.CASH,
                    ValidDiscountIntervalConfiguration.IntervalConfig(0.5, 0.9)
                )
            }
        )
        val service = DiscountService(config)

        assertThrows<BadRequestException> {
            service.validatePriceModifierForPaymentMethod(7.0, PaymentMethod.CASH)
        }
    }
}