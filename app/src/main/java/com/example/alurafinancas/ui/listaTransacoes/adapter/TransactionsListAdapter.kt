package com.example.alurafinancas.ui.listaTransacoes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.alurafinancas.R
import com.example.alurafinancas.data.enumList.Type
import com.example.alurafinancas.data.model.Transaction
import com.example.alurafinancas.databinding.TransacaoItemBinding
import com.example.alurafinancas.extension.brazilianCurrencyFormat
import com.example.alurafinancas.extension.brazilianDateFormat
import com.example.alurafinancas.extension.stringLimit

class TransactionsListAdapter(
    private val list: ArrayList<Transaction>,
    private val context: Context,
    private val itemClick: (Transaction, Int) -> Unit,
    private val deleteItem: (Int) -> Unit
) :
    RecyclerView.Adapter<TransactionsListAdapter.TransactionsListViewHolder>() {

    inner class TransactionsListViewHolder(val binding: TransacaoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setItemClick(transaction: Transaction, position: Int) {
            binding.cardTransaction.setOnClickListener { itemClick(transaction, position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TransactionsListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.transacao_item, parent, false
            )
        )

    override fun onBindViewHolder(holder: TransactionsListViewHolder, position: Int) {
        val item = list[position]

        holder.binding.transacaoValor.setTextColor(
            when (item.type) {
                Type.RECEITA -> ContextCompat.getColor(context, R.color.receita)
                Type.DESPESA -> ContextCompat.getColor(context, R.color.despesa)
            }
        )
        holder.binding.transacaoIcone.setBackgroundResource(
            when (item.type) {
                Type.RECEITA -> R.drawable.icone_transacao_item_receita
                Type.DESPESA -> R.drawable.icone_transacao_item_despesa
            }
        )

        holder.binding.transacaoCategoria.text = item.categoria.stringLimit(limit = 15)
        holder.binding.transacaoData.text = item.data.brazilianDateFormat()
        holder.binding.transacaoValor.text = item.valor.brazilianCurrencyFormat()

        holder.setItemClick(item, position)
        holder.binding.cardTransaction.setOnCreateContextMenuListener { menu, _, _ ->
            menu?.apply {
                add(Menu.NONE, 1, Menu.NONE, "Remover")
                forEach { item ->
                    item.setOnMenuItemClickListener {
                        when (item.itemId) {
                            1 -> deleteItem(position)
                        }
                        false
                    }
                }
            }
        }
    }

    override fun getItemCount() = list.size
}