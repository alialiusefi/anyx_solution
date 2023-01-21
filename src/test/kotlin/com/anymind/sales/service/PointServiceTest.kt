package com.anymind.sales.service

import com.anymind.sales.config.PointMultiplierConfiguration
import com.anymind.sales.entity.PaymentMethod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class PointServiceTest {

    @Test
    fun shouldReturnPointsMultiplierWhenPassingValidArguments() {
        val multiplier = 0.05
        val config = PointMultiplierConfiguration(
            multiplier = buildMap {
                put(
                    PaymentMethod.CASH,
                    multiplier
                )
            }
        )
        val service = PointService(config)

        val actualMultiplier = service.getPointMultiplier(PaymentMethod.CASH)

        assertThat(multiplier).isEqualTo(actualMultiplier)
    }

    @Test
    fun shouldCalculatePointsWhenPassingValidArguments() {
        val multiplier = 0.50
        val config = PointMultiplierConfiguration(
            multiplier = buildMap {
                put(
                    PaymentMethod.CASH,
                    multiplier
                )
            }
        )
        val service = PointService(config)

        val points = service.calculatePoints(BigDecimal(2.0), PaymentMethod.CASH)

        val expectedPoints = 1
        assertThat(expectedPoints).isEqualTo(points)
    }

    @Test
    fun shouldThrowIllegalArgumentExceptionWhenMappedIncorrectly() {
        val config = PointMultiplierConfiguration(
            multiplier = buildMap {}
        )
        val service = PointService(config)

        assertThrows<IllegalArgumentException> {
            service.getPointMultiplier(PaymentMethod.CASH)
        }
    }
}
