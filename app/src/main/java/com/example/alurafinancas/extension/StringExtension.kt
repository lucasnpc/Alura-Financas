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
    if (this.contains("-"))
        this.substring(5)
            .replace(",", ".").toDouble()
    else
        this.substring(3).replace(",", ".").toDouble()

fun String.convertToCalendar(): Date =
    SimpleDateFormat("dd/MM/yyyy").parse(this)

