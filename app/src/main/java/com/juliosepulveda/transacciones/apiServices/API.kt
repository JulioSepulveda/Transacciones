package com.juliosepulveda.transacciones.apiServices

import com.google.gson.GsonBuilder
import com.juliosepulveda.transacciones.apiServices.deserializers.MyDeserializer
import com.juliosepulveda.transacciones.models.Transactions
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//API parsa leer los datos
//Usamos retrofit

//Base de la URL
const private val BASE_URL = "https://api.myjson.com/bins/"

private val gson = GsonBuilder()
    .registerTypeAdapter(Transactions::class.java, MyDeserializer())
    .create()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(gson))
    .baseUrl(BASE_URL)
    .build()

object API {
    val transactionsService: TransactionsService by lazy {
        retrofit.create(TransactionsService::class.java)
    }
}