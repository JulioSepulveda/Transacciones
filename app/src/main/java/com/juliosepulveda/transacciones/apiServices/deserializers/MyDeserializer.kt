package com.juliosepulveda.transacciones.apiServices.deserializers

import android.annotation.SuppressLint
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.juliosepulveda.transacciones.models.Transactions
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

//Deserializador personalizado para filtrar las transacciones con un formato de fecha erroneo
class MyDeserializer : JsonDeserializer<Transactions> {

    private lateinit var transaction: Transactions
    var correctDate = Date()

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Transactions? {
        val jsonObject = json.asJsonObject

        //valida el formato de las fechas
        if (validateDates(jsonObject.get("date").asString)) {
            val fee: Double
            //Comprobamos que el fee no sea nulo, si lo es ponemos 0
            if (jsonObject.get("fee") == null)
                fee = 0.0
            else
                fee = jsonObject.get("fee").asDouble

            val description: String
            //Comprobamos que la descripción no sea nulo, si lo es ponemos ""
            if (jsonObject.get("description") == null || jsonObject.get("description").isJsonNull)
                description = ""
            else
                description = jsonObject.get("description").asString

            //Guardamos la transacción
            transaction = Transactions(
                id = jsonObject.get("id").asString,
                date = correctDate,
                amount = jsonObject.get("amount").asDouble,
                fee = fee,
                desc = description)

            return transaction
        }
        //Si la fecha no tiene un formato correcto devolvemos nulo para no tenerla en cuenta para mostrar
        else
            return null
    }

    //Si la fecha tiene un formato correcto continuamos con el guardado de la transacción. Si no, no la guardamos
    @SuppressLint("SimpleDateFormat")
    private fun validateDates(date: String): Boolean {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

        try {
            correctDate = formatter.parse(date)
        } catch (e: ParseException) {
            return false
        }
        return true
    }
}