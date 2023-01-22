package com.anymind.sales.controller

import com.anymind.sales.dto.AggregatedSale
import com.anymind.sales.dto.SaleCreated
import com.anymind.sales.entity.PaymentMethod
import com.anymind.sales.exception.BadRequestException
import com.anymind.sales.service.SalesService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import javax.validation.constraints.Pattern

@Controller
class SalesController(
    private val salesService: SalesService
) {
    @MutationMapping
    fun createSale(
        @Argument @Pattern(regexp = "^\\d+(\\.\\d+)?\$" , message = "price must match \"{regexp}\"") price: String,
        @Argument priceModifier: Double,
        @Argument paymentMethod: String,
        @Argument datetime: String
    ): SaleCreated {
        val paymentMethodEnum = PaymentMethod.values().find { it.name == paymentMethod } ?: throw BadRequestException(
            "PaymentMethod=$paymentMethod is not supported! Supported paymentMethods=${PaymentMethod.values()}"
        )
        return salesService.createSale(
            price = BigDecimal(price),
            priceModifier = priceModifier,
            paymentMethod = paymentMethodEnum,
            datetime = parseDateTime(datetime)
        )
    }

    @QueryMapping
    fun getHourlySales(@Argument fromDateTime: String, @Argument toDateTime: String): List<AggregatedSale> {
        return salesService.getHourlySales(
            parseDateTime(fromDateTime),
            parseDateTime(toDateTime)
        )
    }

    fun parseDateTime(dateTime: String): LocalDateTime {
        return try {
            LocalDateTime.parse(dateTime)
        } catch (e: DateTimeParseException) {
            throw BadRequestException("Couldn't parse datetime=$dateTime, please check the format.")
        }
    }
}
