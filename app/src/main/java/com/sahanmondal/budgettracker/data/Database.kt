package com.sahanmondal.budgettracker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Transaction::class], version = 1)
abstract class Database : RoomDatabase() {

    abstract fun transactionDao() : TransactionDao
}