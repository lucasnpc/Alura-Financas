package com.example.alurafinancas.ui.listaTransacoes.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

class AddTransactionsDialog(
    private val context: Context,
    parent: ViewGroup,
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
        transactionsRepository: TransactionsRepository,
        type: Type
    ): AlertDialog? {

        dialogBinding.formTransacaoCategoria.adapter =
            if (type == Type.RECEITA) adapterReceita else adapterDespesa

        dialogBinding.formTransacaoData.apply {
            setText(getInstance().brazilianDateFormat())
            setOnClickListener { openDatePicker(it as EditText) }
        }

        return MaterialAlertDialogBuilder(context)
            .setTitle(
                "Adicionar ${
                    if (type == Type.RECEITA) context.resources.getString(R.string.receita) else
                        context.resources.getString(R.string.despesa)
                }"
            )
            .setView(dialogBinding.root)
            .setPositiveButton("Adicionar") { _, _ ->
                val valor = dialogBinding.formTransacaoValor.text.toString()
                val data = getInstance()
                dialogBinding.formTransacaoData.text.toString().convertToCalendar()
                    ?.let { data.time = it }
                transactionsRepository.addTransaction(
                    Transaction(
                        type = type,
                        valor = if (valor == "") BigDecimal.ZERO else BigDecimal(valor),
                        data = data,
                        categoria = dialogBinding.formTransacaoCategoria.selectedItem.toString()
                    ), _result
                )
                dialogBinding.formTransacaoValor.setText("")
            }
            .setNegativeButton("Cancelar", null).show()
    }

    private fun openDatePicker(editData: EditText) {
        val year = getInstance().get(YEAR)
        val month = getInstance().get(MONTH)
        val day = getInstance().get(DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, _, _, dayOfMonth ->
                val date = getInstance()
                date.set(year, month, dayOfMonth)
                editData.setText(date.brazilianDateFormat())
            }, year, month, day
        ).show()
    }

}