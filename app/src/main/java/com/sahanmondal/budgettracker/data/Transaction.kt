package com.sahanmondal.budgettracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transactions_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val amount: Double,
    val description: String
) : Serializable {

}