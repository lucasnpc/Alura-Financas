package com.example.alurafinancas.data.model

import com.example.alurafinancas.data.enumList.Type
import java.math.BigDecimal
import java.util.*

data class Transaction(
    val valor: BigDecimal,
    val categoria: String = "Indefinida",
    val type: Type,
    val data: Calendar = Calendar.getInstance()
)