package hu.gyuriczaadam.sprintformteszt.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hu.gyuriczaadam.sprintformteszt.data.local.entities.TransactionListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactionlistentity")
    fun getTransactions():Flow<List<TransactionListEntity>>

    @Query("SELECT * FROM transactionlistentity WHERE summary  LIKE '%'||:query||'%' OR sum LIKE '%'||:query||'%' OR category LIKE '%'||:query||'%' ")
    fun getTransactionsByQuery(query:String):Flow<List<TransactionListEntity>>


    @Query("SELECT * FROM transactionlistentity WHERE id = :query")
    suspend fun getTransactionById(query: Int):TransactionListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transactionListEntity: TransactionListEntity)
}