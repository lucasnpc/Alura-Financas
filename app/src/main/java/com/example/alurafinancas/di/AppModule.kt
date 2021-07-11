package com.example.alurafinancas.di

import com.example.alurafinancas.data.TransactionsRepository
import com.example.alurafinancas.ui.listaTransacoes.TransactionsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val transactionsModule = module {
    factory { TransactionsRepository() }
    viewModel { TransactionsListViewModel(transactionsRepository = get()) }
}