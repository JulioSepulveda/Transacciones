package com.juliosepulveda.transacciones.apiServices

import com.juliosepulveda.transacciones.models.Transactions
import retrofit2.Call
import retrofit2.http.GET

interface TransactionsService {

    //Recogida de todos los datos
    @GET("1a30k8/")
    fun getAll(): Call<List<Transactions>>


}