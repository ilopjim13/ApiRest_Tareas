package com.es.aplicacion.domain

data class Municipio(
    var CMUM:String,
    val CPRO:String,
    val CUN:String,
    val DMUN50:String
) {
    private var cont = 0
    init {
        CMUM = cont++.toString()
    }
}