package com.anymind.sales.repository.mapper

import com.anymind.sales.dto.AggregatedSale
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.LocalDateTime

class AggregatedSaleRowMapper: RowMapper<AggregatedSale> {
    override fun mapRow(rs: ResultSet, rowNum: Int): AggregatedSale {
        return AggregatedSale(
            points = rs.getLong("points"),
            sales = rs.getString("sales"),
            datetime = LocalDateTime.parse(rs.getString("datetime"))
        )
    }
}