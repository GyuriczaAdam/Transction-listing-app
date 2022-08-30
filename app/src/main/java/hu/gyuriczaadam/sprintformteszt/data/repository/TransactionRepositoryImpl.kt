package hu.gyuriczaadam.sprintformteszt.data.repository

import hu.gyuriczaadam.sprintformteszt.data.local.daos.TransactionDao
import hu.gyuriczaadam.sprintformteszt.data.remote.TransactionApi
import hu.gyuriczaadam.sprintformteszt.data.remote.transaction_dto.TransactionDto
import hu.gyuriczaadam.sprintformteszt.domain.model.TransactionItem
import hu.gyuriczaadam.sprintformteszt.domain.model.local.TransactionListEntity
import hu.gyuriczaadam.sprintformteszt.domain.model.local.toTransacrtionItem
import hu.gyuriczaadam.sprintformteszt.domain.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl(
    private val transactionApi: TransactionApi,
    private val transactionDao: TransactionDao
    ):TransactionRepository {
    override suspend fun getTransactionsFromApi(): TransactionDto {
        return transactionApi.getTransactionList()
    }

    override fun getAllTransactions(): Flow<List<TransactionListEntity>> {
        return transactionDao.getTransactions()
    }

    override suspend fun insertTransaction(transactionListEntity: TransactionListEntity) {
        transactionDao.insertTransaction(transactionListEntity)
    }
}