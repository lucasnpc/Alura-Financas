package com.example.alurafinancas.extension

import java.text.DateFormat
import java.util.*

fun Calendar.brazilianDateFormat(): String =
    DateFormat.getDateInstance(DateFormat.MEDIUM, Locale("pt", "br")).format(this.time)