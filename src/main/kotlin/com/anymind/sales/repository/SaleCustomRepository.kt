package com.anymind.sales.repository

import com.anymind.sales.dto.AggregatedSale
import com.anymind.sales.repository.mapper.AggregatedSaleRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class SaleCustomRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    fun getAggregatedSalesHourlyInRange(from: LocalDateTime, to: LocalDateTime): List<AggregatedSale> {
        return jdbcTemplate.query(
            """
            SELECT date_trunc('hour', datetime) as datetime, sum(price * price_modifier) as sales, 
                    sum(price * point_multiplier) as points from sales where (datetime >= ? and datetime <= ?) 
                    group by datetime,
                        date_part('year', datetime),
                        date_part('month', datetime),
                        date_part('day', datetime),
                        date_part('hour', datetime)
        """.trimIndent(), AggregatedSaleRowMapper(), from, to
        )
    }
}
