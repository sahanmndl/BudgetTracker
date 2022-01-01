package com.sahanmondal.budgettracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sahanmondal.budgettracker.data.Transaction
import com.sahanmondal.budgettracker.ui.transaction.TransactionAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var transactions: ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactions = arrayListOf(
            Transaction(0, "Sprite", 15.00, "LoL"),
            Transaction(1, "Sprite", -15.00, "LoL"),
            Transaction(2, "Sprite", 15.00, "LoL"),
            Transaction(3, "Sprite", -15.00, "LoL"),
            Transaction(4, "Sprite", 15.00, "LoL"),
            Transaction(5, "Sprite", 15.00, "LoL"),
            Transaction(6, "Sprite", -15.00, "LoL"),
            Transaction(7, "Sprite", 15.00, "LoL"),
            Transaction(8, "Sprite", 15.00, "LoL"),
            Transaction(9, "Sprite", -15.00, "LoL"),
            Transaction(10, "Sprite", 15.00, "LoL"),
            Transaction(11, "Sprite", 15.00, "LoL"),
            Transaction(12, "Sprite", -15.00, "LoL"),
        )

        transactionAdapter = TransactionAdapter(transactions)
        linearLayoutManager = LinearLayoutManager(this)

        rvTransactions.apply {
            adapter = transactionAdapter
            layoutManager = linearLayoutManager
        }

        updateData()
    }

    private fun updateData() {
        val netBalance: Double = transactions.map { it.amount }.sum()
        val netSaving: Double = transactions.filter { it.amount > 0 }.map { it.amount }.sum()
        val netExpense: Double = netBalance - netSaving

        tvNetBalance.text = "₹%.2f".format(netBalance)
        tvSavings.text = "₹%.2f".format(netSaving)
        tvExpenses.text = "₹%.2f".format(netExpense)
    }
}