package com.juliosepulveda.transacciones.listeners

import com.juliosepulveda.transacciones.models.Transactions

//MEJORA --> Listener para abrir una pantalla de detalle de las transacciones
interface RecyclerTransactionListener{
    fun onClick(transac: Transactions, position: Int)
}