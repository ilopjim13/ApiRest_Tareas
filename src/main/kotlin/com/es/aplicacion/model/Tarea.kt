package com.es.aplicacion.model

import org.springframework.data.annotation.Id
import java.util.Date

data class Tarea(
    val titulo:String,
    val descripcion:String,
    val fecha:Date,
    var estado:Boolean,
    val creador:String,
    @Id
    var id: String? = null,
    )