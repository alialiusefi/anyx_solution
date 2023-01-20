package com.anymind.sales.repository

import com.anymind.sales.entity.Sale
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SaleRepository : JpaRepository<Sale, UUID>