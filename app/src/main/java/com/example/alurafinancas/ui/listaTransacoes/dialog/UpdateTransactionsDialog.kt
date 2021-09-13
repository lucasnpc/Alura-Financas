package com.example.alurafinancas.ui.listaTransacoes.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
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
import java.math.BigDecimal
import java.util.*

class UpdateTransactionsDialog(
    private val context: Context,
    parent: ViewGroup
) {

    private val dialogBinding: FormTransacaoBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.form_transacao,
        parent,
        false
    )
    private val adapterReceita
        get() = ArrayAdapter.createFromResource(
            context,
            R.array.categorias_de_receita,
            android.R.layout.simple_spinner_dropdown_item
        )
    private val adapterDespesa
        get() = ArrayAdapter.createFromResource(
            context, R.array.categorias_de_despesa,
            android.R.layout.simple_spinner_dropdown_item
        )

    fun createDialog(
        _result: MutableLiveData<TransactionsListResult>,
        repository: TransactionsRepository,
        type: Type,
        transaction: Transaction,
        position: Int
    ): AlertDialog? {
        dialogBinding.formTransacaoValor.setText(String.format(transaction.valor.toString()))
        dialogBinding.formTransacaoCategoria.apply {
            adapter = if (type == Type.RECEITA) adapterReceita else adapterDespesa
            setSelection(
                if (type == Type.RECEITA) context.resources.getStringArray(R.array.categorias_de_receita)
                    .indexOf(transaction.categoria) else context.resources.getStringArray(R.array.categorias_de_despesa)
                    .indexOf(transaction.categoria), true
            )
        }

        dialogBinding.formTransacaoData.apply {
            setText(transaction.data.brazilianDateFormat())
            setOnClickListener { openDatePicker(it as EditText) }
        }


        return AlertDialog.Builder(context)
            .setTitle(
                "Alterar ${
                    if (type == Type.RECEITA) context.resources.getString(R.string.receita) else
                        context.resources.getString(R.string.despesa)
                }"
            )
            .setView(dialogBinding.root)
            .setPositiveButton("Alterar") { _, _ ->
                val valor = dialogBinding.formTransacaoValor.text.toString()
                val data = Calendar.getInstance()
                dialogBinding.formTransacaoData.text.toString().convertToCalendar()
                    ?.let { data.time = it }
                repository.updateTransactions(
                    Transaction(
                        type = type,
                        valor = if (valor == "") BigDecimal.ZERO else BigDecimal(valor),
                        data = data,
                        categoria = dialogBinding.formTransacaoCategoria.selectedItem.toString()
                    ), _result,
                    position = position
                )
                dialogBinding.formTransacaoValor.setText("")
            }
            .setNegativeButton("Cancelar", null).show()
    }

    private fun openDatePicker(editData: EditText) {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, _, _, dayOfMonth ->
                val date = Calendar.getInstance()
                date.set(year, month, dayOfMonth)
                editData.setText(date.brazilianDateFormat())
            }, year, month, day
        ).show()
    }
}