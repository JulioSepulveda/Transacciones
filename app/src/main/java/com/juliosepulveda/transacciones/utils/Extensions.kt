package com.juliosepulveda.transacciones.utils

import android.view.LayoutInflater
import android.view.ViewGroup

/*Infla los adaptadores*/
fun ViewGroup.inflate(layoutId: Int) = LayoutInflater.from(context).inflate(layoutId,this,false)!!
