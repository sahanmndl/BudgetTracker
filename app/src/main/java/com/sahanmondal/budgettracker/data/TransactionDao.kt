package com.sahanmondal.budgettracker.data

import androidx.room.*

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions_table")
    fun getAllTransactions(): List<Transaction>

    @Insert
    fun insertTransaction(vararg transaction: Transaction)

    @Delete
    fun deleteTransaction(transaction: Transaction)

    @Update
    fun updateTransaction(vararg transaction: Transaction)
}