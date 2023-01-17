package com.anymind.sales

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class SalesApplication

fun main(args: Array<String>) {
    runApplication<SalesApplication>(*args)
}
