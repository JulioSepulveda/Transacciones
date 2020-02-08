package com.juliosepulveda.transacciones.models

import java.util.*

//Módelo de datos que se van a leer
data class Transactions (
    var id: String,
    var date: Date,
    var amount: Double,
    var fee: Double?,
    var desc: String?
    )