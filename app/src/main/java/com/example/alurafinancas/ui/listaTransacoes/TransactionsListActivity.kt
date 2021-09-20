package com.example.alurafinancas.ui.listaTransacoes

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.alurafinancas.R
import com.example.alurafinancas.data.enumList.Type
import com.example.alurafinancas.data.model.Transaction
import com.example.alurafinancas.databinding.ActivityListaTransacoesBinding
import com.example.alurafinancas.ui.listaTransacoes.adapter.TransactionsListAdapter
import com.example.alurafinancas.ui.listaTransacoes.dialog.AddTransactionsDialog
import com.example.alurafinancas.ui.listaTransacoes.dialog.UpdateTransactionsDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TransactionsListActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityListaTransacoesBinding.inflate(layoutInflater)
    }

    private val transactionsListViewModel: TransactionsListViewModel by viewModel()

    private val itemDecoration by lazy {
        DividerItemDecoration(this, DividerItemDecoration.VERTICAL).apply {
            ResourcesCompat.getDrawable(resources, R.drawable.divider, theme)
                ?.let { setDrawable(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.listaTransacoesResumo.viewmodel = transactionsListViewModel
        binding.listaTransacoesResumo.lifecycleOwner = this
        binding.transactionsRecyclerView.addItemDecoration(itemDecoration)

        transactionsListViewModel.result.observe(this, Observer {
            val result = it ?: return@Observer

            if (result.success) {
                if (result.transactions != null) {
                    binding.transactionsRecyclerView.adapter = TransactionsListAdapter(
                        result.transactions,
                        this@TransactionsListActivity,
                        itemClick = { transaction, pos ->
                            openUpdateDialog(transaction, pos)
                        },
                        deleteItem = { position ->
                            transactionsListViewModel.deleteTransactionAt(position)
                        }
                    )
                    transactionsListViewModel.verifyTotalState(
                        transactionsListViewModel.sumTotal()
                    )
                }
            }

            if (result.failed)
                Toast.makeText(this, "Erro na transação!", Toast.LENGTH_LONG)
                    .show()

            binding.listaTransacoesAdicionaMenu.close(true)
        })

        binding.listaTransacoesAdicionaReceita.setOnClickListener {
            openAddDialog(Type.RECEITA)
        }

        binding.listaTransacoesAdicionaDespesa.setOnClickListener {
            openAddDialog(Type.DESPESA)
        }
    }

    private fun openAddDialog(type: Type) =
        transactionsListViewModel.openDialog(
            type = type,
            addTransactionsDialog = AddTransactionsDialog(this, binding.root as ViewGroup)
        )

    private fun openUpdateDialog(
        transaction: Transaction,
        pos: Int
    ) =
        transactionsListViewModel.openDialog(
            transactionToUpdate = transaction,
            type = transaction.type,
            updateTransactionsDialog = UpdateTransactionsDialog(
                this,
                binding.root as ViewGroup
            ),
            position = pos
        )

}