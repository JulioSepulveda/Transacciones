package com.juliosepulveda.transacciones.activities

import android.annotation.SuppressLint
import android.graphics.Color
import com.juliosepulveda.transacciones.listeners.RecyclerTransactionListener
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.juliosepulveda.transacciones.apiServices.API
import com.juliosepulveda.transacciones.R
import com.juliosepulveda.transacciones.adapters.TransactionAdapter
import com.juliosepulveda.transacciones.models.Transactions
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var mRecycler: RecyclerView
    private lateinit var mAdapter: TransactionAdapter
    private val layoutManager by lazy { LinearLayoutManager(this) }

    private var orderList = ArrayList<Transactions>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Control del Recycler View
        mRecycler = this.rv_transactions as RecyclerView

        //Carga de datos de la API y filtrado
        getTransactionsInformation()
    }

    private fun getTransactionsInformation() {
        //LLamada a la API
        API
            .transactionsService
            .getAll()
            .enqueue(object : Callback<List<Transactions>> {
                //Recogida de datos correcta
                override fun onResponse(call: Call<List<Transactions>>, response: Response<List<Transactions>>) {
                    //Validar que los id no están repetidos y ordenar de forma descendente por fecha
                    validateOrderData(response.body().filterNotNull())
                    //Obtención de la última transacción para mostrarla en la cabecera de la pantalla
                    extractFirstData()

                    //Carga del recycler view con el resto de transacciones
                    setRecyclerView(orderList)
                }
                //Error en la recogida de datos
                override fun onFailure(call: Call<List<Transactions>>, t: Throwable) {
                    t.printStackTrace()
                    Log.e(R.string.errorLoadData.toString(), t.message)
                }
            })
    }

    //Valida y ordena los registros obtenidos de la API
    private fun validateOrderData(listDataApi: List<Transactions>) {

        //Bucle para recorrer los datos obtenidos y filtrar los id repetidos
        for (transac in listDataApi) {
            val repeatTransac = orderList.find { it.id == transac.id }
            //Si existe el mismo id en los registros ya almacenados y la fecha es menor lo borra
            if (repeatTransac != null && repeatTransac.date < transac.date) {
                orderList.removeAll { it.id == transac.id }
            }

            //Si el nuevo registro no está repetido o la fecha del nuevo registro es mayor a la del registro almacenado lo guarda en la lista
            if (repeatTransac == null || repeatTransac.date < transac.date){
                orderList.add(
                    Transactions(
                        transac.id,
                        transac.date,
                        transac.amount,
                        transac.fee,
                        transac.desc
                    )
                )
            }
        }
        //Ordena la lista resultante por fecha de forma descendente
        orderList.sortByDescending { it.date}
    }

    //Obtenemos el primer registro y lo escribimos en la cabecera de la pantalla
    @SuppressLint("SimpleDateFormat")
    private fun extractFirstData(){
        val formatter = SimpleDateFormat("dd\nMMM\nYYYY")

        //Escribimos los datos
        tvLastDate.text = formatter.format(orderList[0].date)
        tvLastIdValue.text = orderList[0].id
        tvLastAmountValue.text = (orderList[0].amount - orderList[0].fee!!).toString()

        //Cambiamos el color del campo Amount (azul si positivo o rojo si negativo)
        if (orderList[0].amount >= 0)
                tvLastAmountValue.setTextColor(Color.BLUE)
        else
            tvLastAmountValue.setTextColor(Color.RED)

        orderList.removeAt(0)
    }

    //Rellenamos el resto de transacciones en el recycler view
    private fun setRecyclerView(list: List<Transactions>) {
        mRecycler.setHasFixedSize(true)
        mRecycler.itemAnimator = DefaultItemAnimator()
        mRecycler.layoutManager = layoutManager
        mAdapter = (TransactionAdapter(list, object : RecyclerTransactionListener {
            override fun onClick(transac: Transactions, position: Int) {

                //MEJORA: Abrimos una pantalla para mostrar la información de la transacción
                //Hora, descripcion, fee, amount
            }
        }))
        mRecycler.adapter = mAdapter
    }
}

