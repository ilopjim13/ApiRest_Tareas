package com.es.aplicacion.repository

import com.es.aplicacion.model.Tarea

interface TareaCustomRepository {
    fun findAllByUsername(username:String) :List<Tarea>
}