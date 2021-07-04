package com.example.alurafinancas.extension

fun String.stringLimit(limit: Int) =
    if (this.length > limit) "${
        this.substring(
            startIndex = 0,
            endIndex = limit
        )
    }..." else this