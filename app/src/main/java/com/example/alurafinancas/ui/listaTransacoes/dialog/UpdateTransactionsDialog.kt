package com.example.alurafinancas.ui.listaTransacoes.dialog

import android.content.Context
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import com.example.alurafinancas.R
import com.example.alurafinancas.data.TransactionsRepository
import com.example.alurafinancas.data.enumList.Type
import com.example.alurafinancas.data.model.Transaction
import com.example.alurafinancas.extension.brazilianDateFormat
import com.example.alurafinancas.ui.listaTransacoes.TransactionsListResult
import java.util.*

class UpdateTransactionsDialog(
    context: Context,
    parent: ViewGroup
) : TransactionsDialog(context, parent) {

    override val dialogTitle: String
        get() = "Alterar"

    override fun initializeDialogFields(type: Type, transaction: Transaction?) {
        dialogBinding.formTransacaoValor.setText(transaction?.let { String.format(it.valor.toString()) })
        dialogBinding.formTransacaoCategoria.apply {
            adapter = ArrayAdapter.createFromResource(
                context, if (type == Type.RECEITA) arrayReceita else arrayDespesa,
                android.R.layout.simple_spinner_dropdown_item
            )
            transaction?.let {
                setSelection(
                    if (type == Type.RECEITA) context.resources.getStringArray(R.array.categorias_de_receita)
                        .indexOf(transaction.categoria) else context.resources.getStringArray(R.array.categorias_de_despesa)
                        .indexOf(transaction.categoria), true
                )
            }
        }
        dialogBinding.formTransacaoData.apply {
            setText(
                transaction?.data?.brazilianDateFormat() ?: Calendar.getInstance()
                    .brazilianDateFormat()
            )
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
        initializeDialogFields(type, transaction)
        return super.createDialog(result, repository, type, transaction, updatePosition)
    }

}