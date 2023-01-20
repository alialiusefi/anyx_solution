package com.anymind.sales.repository

import com.anymind.sales.dto.AggregatedSale
import com.anymind.sales.repository.mapper.AggregatedSaleRowMapper
import org.springframework.data.repository.CrudRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
open class SaleCustomRepository(
    private val jdbcTemplate: JdbcTemplate
) {
    fun getAggregatedSalesHourlyInRange(from: LocalDateTime, to: LocalDateTime): List<AggregatedSale> {
        return jdbcTemplate.query(
            """
            SELECT date_part('hour', datetime), sum(price * price_modifier) as sales, 
                    sum(price * point_multiplier) as points from sales where (datetime <= ? and datetime >= ?) 
                    group by date_part('hour', datetime)
        """.trimIndent(), AggregatedSaleRowMapper(), from, to
        )
    }
}
