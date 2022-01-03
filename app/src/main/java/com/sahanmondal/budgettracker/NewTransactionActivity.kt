package com.sahanmondal.budgettracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.sahanmondal.budgettracker.data.Database
import com.sahanmondal.budgettracker.data.Transaction
import kotlinx.android.synthetic.main.activity_new_transaction.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)

        etTitle.addTextChangedListener {
            if (it!!.count() > 0) {
                tilTitle.error = null
            }
        }

        etAmount.addTextChangedListener {
            if (it!!.count() > 0) {
                tilAmount.error = null
            }
        }

        etDescription.addTextChangedListener {
            if (it!!.count() > 0) {
                tilDescription.error = null
            }
        }

        btnClose.setOnClickListener {
            finish()
        }

        addTransaction()
    }

    private fun addTransaction() {
        btnAddTransaction.setOnClickListener {
            val title: String = etTitle.text.toString()
            val amount: Double? = etAmount.text.toString().toDoubleOrNull()
            val description: String = etDescription.text.toString()

            when {
                title.isEmpty() -> {
                    tilTitle.error = "Please enter a title"
                }
                amount == null -> {
                    tilAmount.error = "Please enter a valid amount"
                }
                else -> {
                    /**spTypes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            Toast.makeText(this@NewTransactionActivity, "lol", Toast.LENGTH_LONG).show()
                            val accountType: String =
                                adapterView?.getItemAtPosition(position).toString()
                            when (accountType) {
                                "Savings" -> {
                                    amount = abs(amount!!)
                                    Toast.makeText(
                                        this@NewTransactionActivity,
                                        accountType,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                "Expense" -> {
                                    if (amount!! > 0) {
                                        amount!!.times(-1)
                                    }
                                    Toast.makeText(
                                        this@NewTransactionActivity,
                                        accountType,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }*/

                    val transaction = Transaction(0, title, amount, description)
                    insertTransaction(transaction)
                }
            }
        }
    }

    private fun insertTransaction(transaction: Transaction) {
        val db = Room.databaseBuilder(
            this,
            Database::class.java,
            "transactions_table"
        ).build()

        GlobalScope.launch {
            db.transactionDao().insertTransaction(transaction)
            finish()
        }
    }
}