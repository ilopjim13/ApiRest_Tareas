package com.es.aplicacion.repository

import com.es.aplicacion.model.Tarea
import com.mongodb.client.FindIterable

interface TareaCustomRepository {
    fun findAllByUsername(username:String) :List<Tarea>
    fun findByTituloUsername(titulo:String, username: String): Tarea?
}