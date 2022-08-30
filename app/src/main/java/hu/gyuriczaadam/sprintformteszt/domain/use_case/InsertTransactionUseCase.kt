package hu.gyuriczaadam.sprintformteszt.domain.use_case

import hu.gyuriczaadam.sprintformteszt.data.local.entities.TransactionListEntity
import hu.gyuriczaadam.sprintformteszt.domain.repositories.TransactionRepository
import javax.inject.Inject

class InsertTransactionUseCase  @Inject
constructor(
    private val repository: TransactionRepository
    ) {
    suspend operator fun invoke(transactionListEntity: TransactionListEntity){
        repository.insertTransaction(transactionListEntity)
    }
}