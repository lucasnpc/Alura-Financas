package com.example.alurafinancas.ui.listaTransacoes.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.example.alurafinancas.R
import com.example.alurafinancas.data.TransactionsRepository
import com.example.alurafinancas.data.enumList.Type
import com.example.alurafinancas.data.model.Transaction
import com.example.alurafinancas.databinding.FormTransacaoBinding
import com.example.alurafinancas.extension.brazilianDateFormat
import com.example.alurafinancas.extension.convertToCalendar
import com.example.alurafinancas.ui.listaTransacoes.TransactionsListResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.math.BigDecimal
import java.util.Calendar.*

open class TransactionsDialog(
    private val context: Context,
    parent: ViewGroup,
) {
    open val dialogTitle = ""

    protected val dialogBinding: FormTransacaoBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.form_transacao,
        parent,
        false
    )
    protected val arrayReceita
        get() = R.array.categorias_de_receita

    protected val arrayDespesa
        get() = R.array.categorias_de_despesa

    open fun createDialog(
        result: MutableLiveData<TransactionsListResult>,
        repository: TransactionsRepository,
        type: Type,
        transaction: Transaction? = null,
        updatePosition: Int? = null
    ): AlertDialog = MaterialAlertDialogBuilder(context)
        .setTitle(
            "$dialogTitle ${
                if (type == Type.RECEITA) context.resources.getString(R.string.receita) else
                    context.resources.getString(R.string.despesa)
            }"
        )
        .setView(dialogBinding.root)
        .setPositiveButton(dialogTitle) { _, _ ->
            val valor = dialogBinding.formTransacaoValor.text.toString()
            val data = getInstance()
            val createdTransaction = Transaction(
                type = type,
                valor = if (valor == "") BigDecimal.ZERO else BigDecimal(valor),
                data = data,
                categoria = dialogBinding.formTransacaoCategoria.selectedItem.toString()
            )
            dialogBinding.formTransacaoData.text.toString().convertToCalendar()
                ?.let { data.time = it }
            updatePosition?.let {
                updateTransaction(
                    repository,
                    createdTransaction,
                    result,
                    updatePosition
                )
            }
                ?: addTransaction(
                    repository, createdTransaction, result
                )
            dialogBinding.formTransacaoValor.setText("")
        }
        .setNegativeButton("Cancelar", null).show()

    private fun addTransaction(
        repository: TransactionsRepository,
        transaction: Transaction,
        result: MutableLiveData<TransactionsListResult>
    ) =
        repository.addTransaction(
            transaction, result
        )

    private fun updateTransaction(
        repository: TransactionsRepository,
        transaction: Transaction,
        result: MutableLiveData<TransactionsListResult>,
        position: Int
    ) =
        repository.updateTransactions(
            transaction, result,
            position
        )

    open fun initializeDialogFields(type: Type, transaction: Transaction? = null) {}

    protected fun openDatePicker(editData: EditText) =
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = getInstance()
                date.set(year, month, dayOfMonth)
                editData.setText(date.brazilianDateFormat())
            }, getInstance().get(YEAR), getInstance().get(MONTH), getInstance().get(DAY_OF_MONTH)
        ).show()

}