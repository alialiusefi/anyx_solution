package com.anymind.sales.controller

import com.anymind.sales.dto.AggregatedSale
import com.anymind.sales.dto.SaleCreated
import com.anymind.sales.entity.PaymentMethod
import com.anymind.sales.service.SalesService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.math.BigDecimal
import java.time.LocalDateTime

@Controller
class SalesController(
    private val salesService: SalesService
) {
    @MutationMapping
    fun createSale(
        @Argument price: String, @Argument priceModifier: Double, @Argument paymentMethod: PaymentMethod,
        @Argument datetime: String
    ): SaleCreated {
        return salesService.createSale(
            price = BigDecimal(price),
            priceModifier = priceModifier,
            paymentMethod = paymentMethod,
            datetime = LocalDateTime.parse(datetime)
        )
    }

    @QueryMapping
    fun getHourlySales(@Argument fromDateTime: String, @Argument toDateTime: String): List<AggregatedSale> {
        return salesService.getHourlySales(
            LocalDateTime.parse(fromDateTime),
            LocalDateTime.parse(toDateTime)
        )
    }
}
