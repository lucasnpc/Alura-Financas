package com.example.alurafinancas.ui.listaTransacoes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alurafinancas.data.TransactionsRepository
import com.example.alurafinancas.data.enumList.Type
import com.example.alurafinancas.data.model.Transaction
import com.example.alurafinancas.extension.brazilianCurrencyFormat
import com.example.alurafinancas.extension.undoBrazilianFormat
import com.example.alurafinancas.ui.listaTransacoes.dialog.AddTransactionsDialog
import com.example.alurafinancas.ui.listaTransacoes.dialog.UpdateTransactionsDialog
import com.example.alurafinancas.util.VerifyTransactionsListState
import java.math.BigDecimal

class TransactionsListViewModel(val transactionsRepository: TransactionsRepository) : ViewModel() {

    private val _state = MutableLiveData<TransactionsListState>()
    val state: LiveData<TransactionsListState> = _state

    private val _result = MutableLiveData<TransactionsListResult>()
    val result: LiveData<TransactionsListResult> = _result

    fun sumByType(type: Type) =
        _result.value?.transactions?.filter { it.type == type }?.sumOf { it.valor }
            ?.brazilianCurrencyFormat()
            ?: BigDecimal.ZERO.brazilianCurrencyFormat()

    fun sumTotal(): String =
        BigDecimal(
            sumByType(Type.RECEITA).undoBrazilianFormat() - sumByType(Type.DESPESA).undoBrazilianFormat()
        ).brazilianCurrencyFormat()

    fun openDialog(
        transactionToUpdate: Transaction? = null,
        type: Type,
        addTransactionsDialog: AddTransactionsDialog? = null,
        updateTransactionsDialog: UpdateTransactionsDialog? = null,
        position: Int = 0
    ) =
        transactionToUpdate?.let {
            updateTransactionsDialog?.createDialog(
                result = _result,
                repository = transactionsRepository,
                type = type,
                transaction = it,
                updatePosition = position
            )
        }
            ?: addTransactionsDialog?.createDialog(
                result = _result,
                repository = transactionsRepository,
                type = type
            )

    fun verifyTotalState(total: String) =
        VerifyTransactionsListState().verifyState(total, _state)

    fun deleteTransactionAt(position: Int) =
        transactionsRepository.deleteTransaction(_result, position)

}