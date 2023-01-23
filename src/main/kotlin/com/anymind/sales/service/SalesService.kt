package com.anymind.sales.service

import com.anymind.sales.dto.AggregatedSale
import com.anymind.sales.dto.SaleCreated
import com.anymind.sales.entity.PaymentMethod
import com.anymind.sales.entity.Sale
import com.anymind.sales.repository.SaleCustomRepository
import com.anymind.sales.repository.SaleRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@Service
class SalesService(
    private val saleRepository: SaleRepository,
    private val saleCustomRepository: SaleCustomRepository,
    private val discountService: DiscountService,
    private val pointService: PointService
) {
    companion object {
        const val SCALE = 2
        val ROUNDING_MODE = RoundingMode.HALF_UP
    }

    fun createSale(
        price: BigDecimal,
        priceModifier: Double,
        paymentMethod: PaymentMethod,
        datetime: LocalDateTime
    ): SaleCreated {
        discountService.validatePriceModifierForPaymentMethod(priceModifier, paymentMethod)
        val points = pointService.calculatePoints(price, paymentMethod)
        val sale = Sale(
            uuid = UUID.randomUUID(),
            price = price,
            priceModifier = priceModifier,
            paymentMethod = paymentMethod,
            pointMultiplier = pointService.getPointMultiplier(paymentMethod),
            datetime = Timestamp.valueOf(datetime)
        )
        saleRepository.save(sale)
        val finalPrice = price.multiply(BigDecimal(priceModifier))
        return SaleCreated(
            finalPrice = finalPrice.setScale(SCALE, ROUNDING_MODE).toString(),
            points = points
        )
    }

    fun getHourlySales(fromDateTime: LocalDateTime, toDateTime: LocalDateTime): List<AggregatedSale> {
        val aggregatedSales = saleCustomRepository.getAggregatedSalesHourlyInRange(fromDateTime, toDateTime)
        return aggregatedSales.map {
            val salesBigDecimal = BigDecimal(it.sales).setScale(SCALE, ROUNDING_MODE)
            it.copy(sales = salesBigDecimal.toString())
        }
    }
}
