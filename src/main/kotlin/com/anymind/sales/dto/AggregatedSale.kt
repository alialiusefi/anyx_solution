package com.anymind.sales.dto

import java.time.LocalDateTime

data class AggregatedSale(
    val datetime: LocalDateTime,
    val sales: String,
    val points: Long
)