package com.juliosepulveda.transacciones.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.juliosepulveda.transacciones.R
import com.juliosepulveda.transacciones.listeners.RecyclerTransactionListener
import com.juliosepulveda.transacciones.models.Transactions
import com.juliosepulveda.transacciones.utils.inflate
import kotlinx.android.synthetic.main.rv_transactions.view.*
import java.text.SimpleDateFormat

//Adaptador del Recycler View
class TransactionAdapter(private val transact: List<Transactions>, private val listener: RecyclerTransactionListener) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.rv_transactions))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(transact[position], listener)

    override fun getItemCount(): Int = transact.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        fun bind(transact: Transactions, listener: RecyclerTransactionListener) = with(itemView) {

            val formatter = SimpleDateFormat("dd\nMMM\nYYYY")
            //Rellenamos cada uno de los registros en el recycler view
            tvDate.text = formatter.format(transact.date)
            tvId.text = transact.id
            tvAmount.text = (transact.amount - transact.fee!!).toString()

            //Cambiamos el color del campo Amount (azul si positivo o rojo si negativo)
            if (transact.amount >= 0)
                tvAmount.setTextColor(Color.BLUE)
            else
                tvAmount.setTextColor(Color.RED)

            //MEJORA: Abrimos una pantalla para mostrar la información de la transacción
            //Hora, descripcion, fee, amount
            setOnClickListener { listener.onClick(transact, adapterPosition) }
        }
    }

}