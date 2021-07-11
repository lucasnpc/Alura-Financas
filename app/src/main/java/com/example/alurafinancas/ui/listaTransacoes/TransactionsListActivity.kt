package com.example.alurafinancas.ui.listaTransacoes

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.alurafinancas.R
import com.example.alurafinancas.data.enumList.Type
import com.example.alurafinancas.databinding.ActivityListaTransacoesBinding
import com.example.alurafinancas.ui.listaTransacoes.adapter.TransactionsListAdapter
import com.example.alurafinancas.ui.listaTransacoes.dialog.TransactionsDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TransactionsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaTransacoesBinding
    private val transactionsListViewModel: TransactionsListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lista_transacoes)
        binding.listaTransacoesResumo.viewmodel = transactionsListViewModel
        binding.listaTransacoesResumo.lifecycleOwner = this

        transactionsListViewModel.result.observe(this, Observer {
            val result = it ?: return@Observer

            if (result.success) {
                if (result.transactions != null) {
                    binding.listaTransacoesListview.apply {
                        visibility = View.VISIBLE
                        adapter = TransactionsListAdapter(
                            result.transactions,
                            this@TransactionsListActivity
                        )
                        addItemDecoration(dividerItemDecoration())
                    }
                    transactionsListViewModel.verifyTotalState(
                        transactionsListViewModel.sumTotal()
                    )

                    binding.listaTransacoesAdicionaMenu.close(true)
                }
            }
            if (result.failed)
                Toast.makeText(this, "Erro ao adicionar transacao", Toast.LENGTH_SHORT).show()
        })

        binding.listaTransacoesAdicionaReceita.setOnClickListener {
            transactionsListViewModel.openDialog(
                Type.RECEITA,
                TransactionsDialog(this, binding.root as ViewGroup)
            )
        }
        binding.listaTransacoesAdicionaDespesa.setOnClickListener {
            transactionsListViewModel.openDialog(
                Type.DESPESA,
                TransactionsDialog(this, binding.root as ViewGroup)
            )
        }
    }

    private fun dividerItemDecoration(): DividerItemDecoration {
        val itemDecoration = DividerItemDecoration(
            this@TransactionsListActivity,
            DividerItemDecoration.VERTICAL
        )
        itemDecoration.setDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.divider,
                theme
            )!!
        )
        return itemDecoration

    }
}