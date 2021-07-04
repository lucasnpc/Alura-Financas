package com.example.alurafinancas.ui.listaTransacoes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.alurafinancas.R
import com.example.alurafinancas.data.enum.Tipo
import com.example.alurafinancas.data.model.Transacao
import com.example.alurafinancas.databinding.ActivityListaTransacoesBinding
import com.example.alurafinancas.ui.listaTransacoes.adapter.TransactionsListAdapter
import java.math.BigDecimal
import java.util.*

class TransactionsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaTransacoesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lista_transacoes)

        val itemDecoration = DividerItemDecoration(
            this@TransactionsListActivity,
            DividerItemDecoration.VERTICAL
        )
        itemDecoration.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.divider, theme)!!)

        binding.listaTransacoesListview.apply {
            adapter = TransactionsListAdapter(
                transactionsList(),
                this@TransactionsListActivity
            )
            addItemDecoration(itemDecoration)
        }
    }

    private fun transactionsList() = listOf(
        Transacao(
            categoria = "Almoço de final de semana",
            valor = BigDecimal(20.5), tipo = Tipo.DESPESA
        ),
        Transacao(
            valor = BigDecimal(100.0),
            categoria = "Economia",
            tipo = Tipo.RECEITA,

            ),
        Transacao(valor = BigDecimal(200.0), tipo = Tipo.DESPESA),
        Transacao(valor = BigDecimal(500.0), categoria = "Prêmio", tipo = Tipo.RECEITA)
    )
}