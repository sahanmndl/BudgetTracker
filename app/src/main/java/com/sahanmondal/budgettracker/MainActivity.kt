package com.sahanmondal.budgettracker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.sahanmondal.budgettracker.data.Database
import com.sahanmondal.budgettracker.data.Transaction
import com.sahanmondal.budgettracker.ui.transaction.TransactionAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var transactions: List<Transaction>
    private lateinit var ogTransactions: List<Transaction>
    private lateinit var delTransaction: Transaction
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactions = arrayListOf()
        transactionAdapter = TransactionAdapter(transactions)
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        db = Room.databaseBuilder(
            this,
            Database::class.java,
            "transactions_table"
        ).build()

        rvTransactions.apply {
            adapter = transactionAdapter
            layoutManager = linearLayoutManager
        }

        fab.setOnClickListener {
            val intent = Intent(this, NewTransactionActivity::class.java)
            startActivity(intent)
        }

        swipeToDelete()
    }

    private fun updateDashboard() {
        val netBalance: Double = transactions.map { it.amount }.sum()
        val netSaving: Double = transactions.filter { it.amount > 0 }.map { it.amount }.sum()
        val netExpense: Double = netBalance - netSaving

        tvNetBalance.text = "₹%.2f".format(netBalance)
        tvSavings.text = "₹%.2f".format(netSaving)
        tvExpenses.text = "₹%.2f".format(netExpense)
    }

    private fun fetchData() {
        GlobalScope.launch {
            transactions = db.transactionDao().getAllTransactions()

            runOnUiThread {
                updateDashboard()
                transactionAdapter.setData(transactions)
            }
        }
    }

    private fun undoDelete() {
        GlobalScope.launch {
            db.transactionDao().insertTransaction(delTransaction)
            transactions = ogTransactions

            runOnUiThread {
                transactionAdapter.setData(transactions)
                updateDashboard()
            }
        }
    }

    private fun showSnackBar() {
        val view = findViewById<View>(R.id.coordinator)
        val snackBar = Snackbar.make(
            view,
            "Transaction deleted!",
            Snackbar.LENGTH_LONG
        )

        snackBar.setAction("Undo") {
            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(this, R.color.material_red))
            .show()
    }

    private fun deleteTransaction(transaction: Transaction) {
        delTransaction = transaction
        ogTransactions = transactions

        GlobalScope.launch {
            db.transactionDao().deleteTransaction(transaction)
            transactions = transactions.filter { it.id != transaction.id }

            runOnUiThread {
                transactionAdapter.setData(transactions)
                updateDashboard()
                showSnackBar()
            }
        }
    }

    private fun swipeToDelete() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])
            }
        }

        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(rvTransactions)
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }
}