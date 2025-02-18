package com.es.aplicacion.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date

@Document("Tarea")
data class Tarea(
    val titulo:String,
    val descripcion:String,
    val fecha:Date,
    var estado:Boolean,
    val creador:String,
    @BsonId
    var _id: ObjectId? = null,
    )