package com.anymind.sales.dto

import java.time.LocalDateTime

class AggregatedSale(
    val datetime: LocalDateTime,
    val sales: String,
    val points: Long
)