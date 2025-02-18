package com.es.aplicacion.service

import aplicacion.error.exception.BadRequestException
import aplicacion.error.exception.NotFoundException
import com.es.aplicacion.repository.UsuarioRepository
import com.es.aplicacion.dto.TareaAddDTO
import com.es.aplicacion.model.Tarea
import com.es.aplicacion.repository.TareaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class TareaService {

    @Autowired
    private lateinit var tareasRepository: TareaRepository
    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository

    fun getTasks(username:String, auth: Authentication):List<Tarea> {

        usuarioRepository.findByUsername(username).getOrElse { throw  NotFoundException("Este usuario no existe") }
        auth.authorities.forEach {
            if (it.authority != "ROLE_ADMIN" && auth.name != username) throw BadRequestException("No puedes ver las tareas de otros")
        }

        val listaTareas = tareasRepository.findAllByUsername(username)

        return listaTareas
    }

    fun getAllTasks():List<Tarea> {
        val listaTareas = tareasRepository.findAll()
        return listaTareas
    }

    fun addTask(username: String,tareaAdd:TareaAddDTO, auth: Authentication):Tarea {

        usuarioRepository.findByUsername(username).getOrElse { throw  NotFoundException("Este usuario no existe") }

        auth.authorities.forEach {
            if (it.authority != "ROLE_ADMIN" && auth.name != username) throw BadRequestException("No puedes agregar una tarea a otro")
        }

        if(tareaAdd.titulo.isBlank() || tareaAdd.descripcion.isBlank()) throw BadRequestException("Los campos deben estar rellenos")

        val tareas = tareasRepository.findAllByUsername(username)

        tareas.forEach {
            if (tareaAdd.titulo == it.titulo) throw BadRequestException("No pueden haber dos tareas con el mismo nombre")
        }

        val tareaNew = Tarea(tareaAdd.titulo, tareaAdd.descripcion, Date(), false, username)

        tareasRepository.save(tareaNew)

        return tareaNew
    }

    fun updateState(titulo: String, username: String, auth: Authentication):Tarea {

        val tareaUpdate = tareasRepository.findByTituloUsername(titulo,username)
            ?: throw NotFoundException("No existe esta tarea")

        auth.authorities.forEach {
            if (it.authority != "ROLE_ADMIN" && auth.name != tareaUpdate.creador) throw BadRequestException("No puedes actualizar la tarea de otro")
        }
        tareaUpdate.estado = !tareaUpdate.estado

        tareasRepository.save(tareaUpdate)

        return tareaUpdate
    }

    fun deleteTask(titulo: String, username: String, auth: Authentication) {
        val tareaDelete = tareasRepository.findByTituloUsername(titulo,username)
            ?: throw NotFoundException("No existe esta tarea")
        auth.authorities.forEach {
            if (it.authority != "ROLE_ADMIN" && auth.name != tareaDelete.creador) throw BadRequestException("No puedes actualizar la tarea de otro")
        }
        tareasRepository.delete(tareaDelete)

    }

}