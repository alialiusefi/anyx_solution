package com.anymind.sales.controller

import com.anymind.sales.config.PointMultiplierConfiguration
import com.anymind.sales.config.ValidDiscountIntervalConfiguration
import com.anymind.sales.dto.AggregatedSale
import com.anymind.sales.entity.PaymentMethod
import com.anymind.sales.entity.Sale
import com.anymind.sales.repository.SaleRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.ResponseError
import org.springframework.graphql.test.tester.HttpGraphQlTester
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = ["server.port=8080"])
@AutoConfigureHttpGraphQlTester
@EnableConfigurationProperties(value = [PointMultiplierConfiguration::class, ValidDiscountIntervalConfiguration::class])
class SalesControllerIntegrationTest(
    @Autowired val salesRepository: SaleRepository,
    @Autowired val graphQlTester: HttpGraphQlTester
) {
    @BeforeEach
    fun beforeEach() {
        salesRepository.deleteAll()
    }

    @Test
    fun shouldReturnSaleCreatedWhenMutationArgumentsAreValid() {
        val timeStamp = Timestamp.valueOf(LocalDateTime.now())
        val expected = Sale(
            price = BigDecimal(100),
            priceModifier = 0.95,
            uuid = UUID.randomUUID(),
            paymentMethod = PaymentMethod.CASH,
            datetime = timeStamp,
            pointMultiplier = 0.05
        )

        graphQlTester.documentName("createSale")
            .variable("price", "100")
            .variable("priceModifier", 0.95f)
            .variable("paymentMethod", "CASH")
            .variable("datetime", timeStamp.toLocalDateTime().toString())
            .execute()
            .path("createSale.finalPrice").entity(String::class.java).isEqualTo("95.00")
            .path("createSale.points").entity(Long::class.java).isEqualTo(5L)

        val actual = salesRepository.findAll()[0]

        val comparison = RecursiveComparisonConfiguration.builder()
            .withIgnoredFields("uuid")
            .build()
        assertThat(expected).usingRecursiveComparison(comparison).isEqualTo(actual)
    }

    @Test
    fun shouldReturnBadRequestErrorMessageWhenPriceModifierIsOutOfRange() {
        graphQlTester.documentName("createSale")
            .variable("price", "100")
            .variable("priceModifier", 101.00f)
            .variable("paymentMethod", "CASH")
            .variable("datetime", LocalDateTime.now().toString())
            .execute()
            .errors().expect {
                it.extensions["classification"] == "BAD_REQUEST"
            }.expect {
                it.path == "createSale"
            }.expect {
                it.message == "priceModifier=101.0 doesnt fit paymentMethod=CASH requirements!"
            }
    }

    @ParameterizedTest
    @MethodSource("createSaleInvalidArguments")
    fun shouldReturnBadRequestErrorMessageWhenInvalidCreateSaleArguments(
        price: String,
        priceModifier: Double,
        paymentMethod: String,
        datetime: String,
        expectations: (responseError: ResponseError) -> Boolean
    ) {
        graphQlTester.documentName("createSale")
            .variable("price", price)
            .variable("priceModifier", priceModifier)
            .variable("paymentMethod", paymentMethod)
            .variable("datetime", datetime)
            .execute()
            .errors().expect {
                expectations.invoke(it)
            }
    }

    @ParameterizedTest
    @MethodSource("getHourlySalesInvalidArguments")
    fun shouldReturnBadRequestErrorMessageWhenInvalidGetHourlySalesArguments(
        fromDateTime: String,
        toDateTime: String,
        expectedErrorMessage: String
    ) {
        graphQlTester.documentName("getHourlySales")
            .variable("fromDateTime", fromDateTime)
            .variable("toDateTime", toDateTime)
            .execute()
            .errors().expect {
                it.extensions["classification"] == "BAD_REQUEST"
            }.expect {
                it.path == "createSale"
            }.expect {
                it.message == expectedErrorMessage
            }
    }

    @Test
    fun shouldReturnListOfAggregatedSalesWhenQueryArgumentsAreValid() {
        val listOfSales = listOfSales()
        salesRepository.saveAll(listOfSales)

        graphQlTester.documentName("getHourlySales")
            .variable("fromDateTime", LocalDateTime.of(2019, 1, 14, 3, 0).toString())
            .variable("toDateTime", LocalDateTime.of(2023, 1, 14, 3, 0).toString())
            .execute()
            .path("getHourlySales")
            .entityList(AggregatedSale::class.java)
            .contains(*expectedListOfAggregatedSales().toTypedArray())
    }

    companion object {
        @JvmStatic
        fun createSaleInvalidArguments(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "abcd",
                    "0.95",
                    "JCB",
                    LocalDateTime.now().toString(),
                    { responseError: ResponseError ->
                        return@of responseError.extensions["classification"] == "BAD_REQUEST" &&
                                responseError.path == "createSale" &&
                                responseError.message?.contains("must match") ?: false
                    }
                ),
                Arguments.of(
                    "100.00",
                    "0.95",
                    "newpaymentmethod",
                    LocalDateTime.now().toString(),
                    { responseError: ResponseError ->
                        return@of responseError.extensions["classification"] == "BAD_REQUEST" &&
                                responseError.path == "createSale" &&
                                responseError.message?.contains(
                                    "PaymentMethod=newpaymentmethod is" +
                                            " not supported! Supported paymentMethods="
                                ) ?: false
                    }
                ),
                Arguments.of(
                    "100.00",
                    "0.95",
                    "JCB",
                    "abcd",
                    { responseError: ResponseError ->
                        return@of responseError.extensions["classification"] == "BAD_REQUEST" &&
                                responseError.path == "createSale" &&
                                responseError.message?.contains(
                                    "Couldn't parse datetime=abcd, " +
                                            "please check the format."
                                ) ?: false
                    }
                )
            )
        }

        @JvmStatic
        fun getHourlySalesInvalidArguments(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "",
                    "",
                    "Couldn't parse datetime=, please check the format."
                ),
                Arguments.of(
                    "abcd",
                    "",
                    "Couldn't parse datetime=abcd, please check the format."
                ),
            )
        }
    }

    fun listOfSales(): List<Sale> = listOf(
        Sale(
            uuid = UUID.randomUUID(),
            price = BigDecimal(100),
            priceModifier = 0.95,
            paymentMethod = PaymentMethod.CASH,
            datetime = Timestamp.valueOf(LocalDateTime.of(2022, 1, 15, 4, 15)),
            pointMultiplier = 0.05
        ),
        Sale(
            uuid = UUID.randomUUID(),
            price = BigDecimal(100),
            priceModifier = 0.95,
            paymentMethod = PaymentMethod.CASH,
            datetime = Timestamp.valueOf(LocalDateTime.of(2022, 1, 15, 4, 15)),
            pointMultiplier = 0.05
        ),
        Sale(
            uuid = UUID.randomUUID(),
            price = BigDecimal(100),
            priceModifier = 0.95,
            paymentMethod = PaymentMethod.CASH,
            datetime = Timestamp.valueOf(LocalDateTime.of(2022, 1, 15, 3, 15)),
            pointMultiplier = 0.05
        ),
        Sale(
            uuid = UUID.randomUUID(),
            price = BigDecimal(100),
            priceModifier = 0.95,
            paymentMethod = PaymentMethod.CASH,
            datetime = Timestamp.valueOf(LocalDateTime.of(2022, 1, 14, 3, 15)),
            pointMultiplier = 0.05
        ),
        Sale(
            uuid = UUID.randomUUID(),
            price = BigDecimal(100),
            priceModifier = 0.95,
            paymentMethod = PaymentMethod.CASH,
            datetime = Timestamp.valueOf(LocalDateTime.of(2022, 2, 15, 3, 15)),
            pointMultiplier = 0.05
        ),
        Sale(
            uuid = UUID.randomUUID(),
            price = BigDecimal(100),
            priceModifier = 0.95,
            paymentMethod = PaymentMethod.CASH,
            datetime = Timestamp.valueOf(LocalDateTime.of(2021, 1, 15, 3, 15)),
            pointMultiplier = 0.05
        )
    )

    fun expectedListOfAggregatedSales(): List<AggregatedSale> = listOf(
        AggregatedSale(
            sales = "190.00",
            points = 10L,
            datetime = LocalDateTime.of(2022, 1, 15, 4, 0)
        ),
        AggregatedSale(
            sales = "95.00",
            points = 5L,
            datetime = LocalDateTime.of(2022, 1, 15, 3, 0)
        ),
        AggregatedSale(
            sales = "95.00",
            points = 5L,
            datetime = LocalDateTime.of(2022, 1, 14, 3, 0)
        ),
        AggregatedSale(
            sales = "95.00",
            points = 5L,
            datetime = LocalDateTime.of(2022, 2, 15, 3, 0)
        ),
        AggregatedSale(
            sales = "95.00",
            points = 5L,
            datetime = LocalDateTime.of(2021, 1, 15, 3, 0)
        )
    )
}
