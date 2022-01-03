package com.sahanmondal.budgettracker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.sahanmondal.budgettracker.data.Database
import com.sahanmondal.budgettracker.data.Transaction
import kotlinx.android.synthetic.main.activity_edit_transaction.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditTransactionActivity : AppCompatActivity() {

    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        etUpTitle.setText(transaction.title)
        etUpAmount.setText(transaction.amount.toString())
        etUpDescription.setText(transaction.description)

        etUpTitle.addTextChangedListener {
            if (it!!.count() > 0) {
                tilUpTitle.error = null
            }

            btnUpdateTransaction.visibility = View.VISIBLE
        }

        etUpAmount.addTextChangedListener {
            if (it!!.count() > 0) {
                tilUpAmount.error = null
            }

            btnUpdateTransaction.visibility = View.VISIBLE
        }

        etUpDescription.addTextChangedListener {
            if (it!!.count() > 0) {
                tilUpDescription.error = null
            }

            btnUpdateTransaction.visibility = View.VISIBLE
        }

        btnClose2.setOnClickListener {
            finish()
        }

        linearLayout.setOnClickListener {
            this.window.decorView.clearFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

        updateTransaction()
    }

    private fun updateTransaction() {
        btnUpdateTransaction.setOnClickListener {
            val title: String = etUpTitle.text.toString()
            val amount: Double? = etUpAmount.text.toString().toDoubleOrNull()
            val description: String = etUpDescription.text.toString()

            when {
                title.isEmpty() -> {
                    tilUpTitle.error = "Please enter a title"
                }
                amount == null -> {
                    tilUpAmount.error = "Please enter a valid amount"
                }
                else -> {
                    val transaction = Transaction(transaction.id, title, amount, description)
                    updateTransaction(transaction)
                }
            }
        }
    }

    private fun updateTransaction(transaction: Transaction) {
        val db = Room.databaseBuilder(
            this,
            Database::class.java,
            "transactions_table"
        ).build()

        GlobalScope.launch {
            db.transactionDao().updateTransaction(transaction)
            finish()
        }
    }
}