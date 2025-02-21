package com.es.aplicacion.service

import com.es.aplicacion.error.exception.BadRequestException
import com.es.aplicacion.error.exception.NotFoundException
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

    /**
     * Obtiene todas las tareas de un usuario específico.
     *
     * @param username Nombre de usuario del propietario de las tareas.
     * @param auth Información de autenticación del usuario actual.
     * @return Lista de tareas del usuario.
     * @throws NotFoundException si el usuario no existe.
     * @throws BadRequestException si un usuario intenta acceder a tareas de otro sin ser administrador.
     */
    fun getTasks(username:String, auth: Authentication):List<Tarea> {
        try {
            usuarioRepository.findByUsername(username).getOrElse { throw  NotFoundException("Este usuario no existe") }
            auth.authorities.forEach {
                if (it.authority != "ROLE_ADMIN" && auth.name != username) throw BadRequestException("No puedes ver las tareas de otros")
            }
        } catch (e:Exception) {
            println(e.message)
        }

        val listaTareas = tareasRepository.findAllByUsername(username)

        return listaTareas
    }

    /**
     * Obtiene todas las tareas de la base de datos.
     *
     * @return Lista de todas las tareas.
     */
    fun getAllTasks():List<Tarea> {
        val listaTareas = tareasRepository.findAll()
        return listaTareas
    }

    /**
     * Agrega una nueva tarea para un usuario.
     *
     * @param username Nombre de usuario al que se le asignará la tarea.
     * @param tareaAdd DTO con los datos de la tarea a agregar.
     * @param auth Información de autenticación del usuario actual.
     * @return La tarea creada.
     * @throws NotFoundException si el usuario no existe.
     * @throws BadRequestException si el usuario intenta agregar una tarea a otro sin ser administrador.
     * @throws BadRequestException si los campos de la tarea están vacíos o si ya existe una tarea con el mismo título.
     */
    fun addTask(username: String,tareaAdd:TareaAddDTO, auth: Authentication):Tarea {
        try {
            usuarioRepository.findByUsername(username).getOrElse { throw  NotFoundException("Este usuario no existe") }
            auth.authorities.forEach {
                if (it.authority != "ROLE_ADMIN" && auth.name != username) throw BadRequestException("No puedes agregar una tarea a otro")
            }

            if(tareaAdd.titulo.isBlank() || tareaAdd.descripcion.isBlank()) throw BadRequestException("Los campos deben estar rellenos")

            tareasRepository.findAllByUsername(username).forEach {
                if (tareaAdd.titulo == it.titulo) throw BadRequestException("No pueden haber dos tareas con el mismo nombre")
            }

        } catch (e:Exception) {
            println(e.message)
        }

        val tareaNew = Tarea(tareaAdd.titulo, tareaAdd.descripcion, Date(), false, username)

        tareasRepository.save(tareaNew)

        return tareaNew
    }

    /**
     * Cambia el estado de una tarea (completada/no completada).
     *
     * @param titulo Título de la tarea a actualizar.
     * @param username Nombre de usuario propietario de la tarea.
     * @param auth Información de autenticación del usuario actual.
     * @return La tarea actualizada.
     * @throws NotFoundException si la tarea no existe.
     * @throws BadRequestException si un usuario intenta modificar una tarea ajena sin permisos.
     */
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

    /**
     * Elimina una tarea de la base de datos.
     *
     * @param titulo Título de la tarea a eliminar.
     * @param username Nombre de usuario propietario de la tarea.
     * @param auth Información de autenticación del usuario actual.
     * @throws NotFoundException si la tarea no existe.
     * @throws BadRequestException si un usuario intenta eliminar una tarea ajena sin permisos.
     */
    fun deleteTask(titulo: String, username: String, auth: Authentication) {
        val tareaDelete = tareasRepository.findByTituloUsername(titulo,username)
            ?: throw NotFoundException("No existe esta tarea")
        auth.authorities.forEach {
            if (it.authority != "ROLE_ADMIN" && auth.name != tareaDelete.creador) throw BadRequestException("No puedes actualizar la tarea de otro")
        }
        tareasRepository.delete(tareaDelete)

    }
}