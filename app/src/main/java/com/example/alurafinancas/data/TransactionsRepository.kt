package com.example.alurafinancas.data

import androidx.lifecycle.MutableLiveData
import com.example.alurafinancas.data.model.Transaction
import com.example.alurafinancas.ui.listaTransacoes.TransactionsListResult
import java.math.BigDecimal

class TransactionsRepository {
    private val transactions: ArrayList<Transaction> = ArrayList()

    fun addTransaction(transaction: Transaction, result: MutableLiveData<TransactionsListResult>) =
        if (transaction.valor == BigDecimal.ZERO) {
            result.value = TransactionsListResult(failed = true)
        } else {
            transactions.add(transaction)
            result.value =
                TransactionsListResult(success = true, transactions = transactions)
        }

    fun updateTransactions(
        transaction: Transaction,
        result: MutableLiveData<TransactionsListResult>,
        position: Int
    ) =
        if (transaction.valor == BigDecimal.ZERO) {
            result.value = TransactionsListResult(failed = true)
        } else {
            transactions[position] = transaction
            result.value =
                TransactionsListResult(success = true, transactions = transactions)
        }

    fun deleteTransaction(result: MutableLiveData<TransactionsListResult>, position: Int) {
        transactions.removeAt(position)
        result.value = TransactionsListResult(success = true, transactions = transactions)
    }
}