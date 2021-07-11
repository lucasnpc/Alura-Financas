package com.example.alurafinancas.extension

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

fun BigDecimal.brazilianCurrencyFormat(): String =
    DecimalFormat.getCurrencyInstance(Locale("pt", "BR")).format(this).replace("-R$", "R$ -")