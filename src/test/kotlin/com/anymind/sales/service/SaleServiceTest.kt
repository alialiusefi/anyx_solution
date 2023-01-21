package com.anymind.sales.service

import com.anymind.sales.dto.AggregatedSale
import com.anymind.sales.dto.SaleCreated
import com.anymind.sales.entity.PaymentMethod
import com.anymind.sales.entity.Sale
import com.anymind.sales.repository.SaleCustomRepository
import com.anymind.sales.repository.SaleRepository
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

class SaleServiceTest {

    private val mockSalesRepository : SaleRepository = mockk()
    private val mockSaleCustomRepository: SaleCustomRepository = mockk()
    private val mockkDiscountService: DiscountService = mockk()
    private val mockPointService: PointService = mockk()
    private val saleService = SalesService(mockSalesRepository, mockSaleCustomRepository, mockkDiscountService,
        mockPointService
    )
    @BeforeEach
    fun beforeEach() {
        clearMocks(
            mockSalesRepository,
            mockSaleCustomRepository,
            mockPointService,
            mockkDiscountService
        )
    }
    @Test
    fun shouldReturnSaleCreatedWhenPassingValidPriceAndPaymentMethod() {
        val points = 3L
        val pointsMultiplier = 0.05
        val uuid = UUID.randomUUID()
        val price = BigDecimal(50.00)
        val priceModifier = 0.95
        val dateTime = LocalDateTime.now()
        val paymentMethod = PaymentMethod.CASH
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns uuid
        every { mockkDiscountService.validatePriceModifierForPaymentMethod(any(), any()) } returns Unit
        every { mockPointService.calculatePoints(any(), any()) } returns points
        every { mockPointService.getPointMultiplier(any()) } returns pointsMultiplier
        val saleSlot = slot<Sale>()
        val expectedSale = Sale(
            uuid = uuid,
            price = price,
            priceModifier = priceModifier,
            paymentMethod = paymentMethod,
            pointMultiplier = 0.05,
            datetime = Timestamp.valueOf(dateTime)
        )
        every { mockSalesRepository.save(capture(saleSlot)) } returns mockk()

        val actualSaleCreated = saleService.createSale(
            price = price,
            priceModifier = priceModifier,
            paymentMethod = paymentMethod,
            datetime = dateTime
        )
        val expectedSaleCreated = SaleCreated(
            finalPrice = BigDecimal(47.50).setScale(2).toString(),
            points = points
        )

        assertEquals(expectedSaleCreated, actualSaleCreated)
        assertEquals(expectedSale, saleSlot.captured)
    }

    @Test
    fun shouldReturnAggregatedSaleListWhenPassingValidArguments() {
        val aggregatedSales = listOf(
            AggregatedSale(
                LocalDateTime.now(),
                "123.123123",
                2L
            ),
            AggregatedSale(
                LocalDateTime.now(),
                "456.55555",
                2L
            )
        )
        every { mockSaleCustomRepository.getAggregatedSalesHourlyInRange(any(), any()) } returns aggregatedSales

        val actualListOfAggregatedSale = saleService.getHourlySales(
            LocalDateTime.now().minusHours(1L), LocalDateTime.now().plusHours(1L)
        )

        val expectedAggregatedSales = listOf(
            AggregatedSale(
                LocalDateTime.now(),
                "123.12",
                2L
            ),
            AggregatedSale(
                LocalDateTime.now(),
                "456.56",
                2L
            )
        )
        assertThat(expectedAggregatedSales).usingRecursiveFieldByFieldElementComparatorIgnoringFields("datetime")
            .isEqualTo(actualListOfAggregatedSale)
    }
}