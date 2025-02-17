package org.example.com.es.aplicacion.model

import java.util.Date

data class Tarea(
    val titulo:String,
    val descripcion:String,
    val fecha:Date,
    val estado:Boolean
    )