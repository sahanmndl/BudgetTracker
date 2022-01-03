package com.sahanmondal.budgettracker.ui.transaction

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.sahanmondal.budgettracker.EditTransactionActivity
import com.sahanmondal.budgettracker.R
import com.sahanmondal.budgettracker.data.Transaction
import kotlin.math.abs

class TransactionAdapter(private var transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    class TransactionHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: MaterialTextView = view.findViewById(R.id.tvTransactionTitle)
        val amount: MaterialTextView = view.findViewById(R.id.tvTransactionAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.amount.context

        holder.title.text = transaction.title

        if (transaction.amount >= 0) {
            holder.amount.text = "+ ₹%.2f".format(transaction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.material_green))
        } else {
            holder.amount.text = "- ₹%.2f".format(abs(transaction.amount))
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.material_red))
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, EditTransactionActivity::class.java)
            intent.putExtra("transaction", transaction)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun setData(transactions: List<Transaction>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }
}