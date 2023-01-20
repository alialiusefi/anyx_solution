package com.anymind.sales.repository.mapper

import com.anymind.sales.dto.AggregatedSale
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.sql.Timestamp

class AggregatedSaleRowMapper: RowMapper<AggregatedSale> {
    override fun mapRow(rs: ResultSet, rowNum: Int): AggregatedSale {
        return AggregatedSale(
            points = rs.getLong("points"),
            sales = rs.getString("sales"),
            datetime = Timestamp.valueOf(rs.getString("datetime")).toLocalDateTime()
        )
    }
}