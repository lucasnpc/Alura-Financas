package com.example.alurafinancas.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.stringLimit(limit: Int) =
    if (this.length > limit) "${
        this.substring(
            startIndex = 0,
            endIndex = limit
        )
    }..." else this

fun String.undoBrazilianFormat(): Double =
    this.substring(startIndex = 3, endIndex = this.length).replace(".", "")
        .replace(",", ".").toDouble()

fun String.convertToCalendar(): Date? =
    SimpleDateFormat("dd/MM/yyyy", Locale("pt", "br")).parse(this)

