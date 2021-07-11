package com.example.alurafinancas.util

import androidx.lifecycle.MutableLiveData
import com.example.alurafinancas.ui.listaTransacoes.TransactionsListState

class VerifyTransactionsListState {
    fun verifyState(total: String, _state: MutableLiveData<TransactionsListState>) =
        if (total.contains("-")) _state.value =
            TransactionsListState(totalGreaterThanZero = false)
        else _state.value = TransactionsListState(totalGreaterThanZero = true)

}
