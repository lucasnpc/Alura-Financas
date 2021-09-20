package com.example.alurafinancas.ui.listaTransacoes.dialog

import android.content.Context
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import com.example.alurafinancas.data.TransactionsRepository
import com.example.alurafinancas.data.enumList.Type
import com.example.alurafinancas.data.model.Transaction
import com.example.alurafinancas.extension.brazilianDateFormat
import com.example.alurafinancas.ui.listaTransacoes.TransactionsListResult
import java.util.Calendar.getInstance

class AddTransactionsDialog(
    context: Context,
    parent: ViewGroup,
) : TransactionsDialog(context, parent) {

    override val dialogTitle: String
        get() = "Adicionar"

    override fun initializeDialogFields(type: Type, transaction: Transaction?) {
        dialogBinding.formTransacaoCategoria.apply {
            adapter = ArrayAdapter.createFromResource(
                context, if (type == Type.RECEITA) arrayReceita else arrayDespesa,
                android.R.layout.simple_spinner_dropdown_item
            )
        }
        dialogBinding.formTransacaoData.apply {
            setText(getInstance().brazilianDateFormat())
            setOnClickListener { openDatePicker(it as EditText) }
        }
    }

    override fun createDialog(
        result: MutableLiveData<TransactionsListResult>,
        repository: TransactionsRepository,
        type: Type,
        transaction: Transaction?,
        updatePosition: Int?
    ): AlertDialog {
        initializeDialogFields(type)
        return super.createDialog(result, repository, type, transaction, updatePosition)
    }

}