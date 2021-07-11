package com.example.alurafinancas.ui.listaTransacoes

import com.example.alurafinancas.data.model.Transaction

data class TransactionsListResult(
    val success: Boolean = false,
    val failed: Boolean = false,
    val transactions: ArrayList<Transaction>? = null
)