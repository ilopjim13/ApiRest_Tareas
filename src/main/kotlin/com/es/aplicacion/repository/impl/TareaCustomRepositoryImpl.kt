package com.es.aplicacion.repository.impl

import com.mongodb.client.model.Filters
import com.es.aplicacion.model.Tarea
import com.es.aplicacion.repository.TareaCustomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate

class TareaCustomRepositoryImpl:TareaCustomRepository {

    @Autowired
    private lateinit var mongoTemplate:MongoTemplate
    override fun findAllByUsername(username: String): List<Tarea> {
        val database = mongoTemplate.db
        val collection = database.getCollection("tarea", Tarea::class.java)

        val filtroUsername = Filters.eq("creador", username)
        val tareas = collection.find(filtroUsername).toList()

        return tareas
    }


}