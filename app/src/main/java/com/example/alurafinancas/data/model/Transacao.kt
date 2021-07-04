package com.example.alurafinancas.data.model

import com.example.alurafinancas.data.enum.Tipo
import java.math.BigDecimal
import java.util.*

data class Transacao(
    val valor: BigDecimal,
    val categoria: String = "Indefinida",
    val tipo: Tipo,
    val data: Calendar = Calendar.getInstance()
)