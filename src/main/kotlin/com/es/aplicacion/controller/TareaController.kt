package com.es.aplicacion.controller

import com.es.aplicacion.dto.TareaAddDTO
import com.es.aplicacion.model.Tarea
import com.es.aplicacion.service.TareaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controlador REST para gestionar las tareas de los usuarios en el sistema.
 * Permite realizar operaciones como mostrar tareas de un usuario, agregar tareas,
 * actualizar el estado de una tarea y eliminar tareas.
 */
@RestController
@RequestMapping("/tarea")
class TareaController {

    @Autowired
    private lateinit var tareaService: TareaService

    /**
     * Obtiene todas las tareas de un usuario específico.
     *
     * Este método recibe el nombre de usuario a través de la ruta y el objeto de autenticación
     * para verificar que el usuario autenticado tiene permisos para acceder a las tareas
     * de ese usuario. Devuelve una lista de tareas.
     *
     * @param username El nombre de usuario cuya tareas se desean obtener.
     * @param authentication El objeto de autenticación que contiene los detalles del usuario autenticado.
     * @return Una respuesta con una lista de tareas del usuario solicitado.
     * @throws BadRequestException Si el usuario no tiene permisos para ver las tareas de otro usuario.
     * @throws NotFoundException Si el usuario no existe.
     */
    @GetMapping("/mostrar/{username}")
    fun getTask(
        @PathVariable username:String,
        authentication: Authentication
    ):ResponseEntity<List<Tarea>> {
        val tareas = tareaService.getTasks(username, authentication)
        return ResponseEntity(tareas, HttpStatus.OK)
    }

    /**
     * Obtiene todas las tareas del sistema, sin filtrar por usuario.
     *
     * Este método devuelve todas las tareas almacenadas en el sistema.
     *
     * @return Una respuesta con una lista de todas las tareas.
     */
    @GetMapping("/mostrarTodas")
    fun getAll(authentication: Authentication):ResponseEntity<List<Tarea>>  {
        return ResponseEntity(tareaService.getAllTasks(), HttpStatus.OK)
    }

    /**
     * Agrega una nueva tarea para un usuario.
     *
     * Este método recibe el nombre de usuario y los datos de la tarea a través del cuerpo de la solicitud.
     * Verifica que el usuario autenticado tiene permisos para agregar tareas a ese usuario.
     * Si la tarea es válida, se agrega y se devuelve la tarea creada.
     *
     * @param username El nombre de usuario al que se le agregará la tarea.
     * @param tareaAdd El objeto con los datos de la nueva tarea a agregar.
     * @param authentication El objeto de autenticación que contiene los detalles del usuario autenticado.
     * @return Una respuesta con la tarea agregada.
     * @throws BadRequestException Si los datos de la tarea no son válidos o el usuario no tiene permisos para agregar tareas.
     * @throws NotFoundException Si el usuario no existe.
     */
    @PostMapping("/agregarTarea/{username}")
    fun addTask(
        @PathVariable username: String,
        @RequestBody tareaAdd: TareaAddDTO,
        authentication: Authentication
    ):ResponseEntity<Tarea>  {
        val tarea = tareaService.addTask(username, tareaAdd, authentication)
        return ResponseEntity(tarea, HttpStatus.OK)
    }

    /**
     * Actualiza el estado de una tarea específica.
     *
     * Este método recibe el título de la tarea y el nombre del usuario a través de la ruta,
     * y actualiza el estado de la tarea (de "completa" a "incompleta" o viceversa).
     * Verifica que el usuario autenticado tiene permisos para actualizar la tarea.
     *
     * @param titulo El título de la tarea que se desea actualizar.
     * @param username El nombre del usuario propietario de la tarea.
     * @param authentication El objeto de autenticación que contiene los detalles del usuario autenticado.
     * @return Una respuesta con la tarea actualizada.
     * @throws BadRequestException Si el usuario no tiene permisos para actualizar la tarea de otro.
     * @throws NotFoundException Si la tarea no existe.
     */
    @PostMapping("/actualizarEstado/{titulo}/{username}")
    fun updateState(
        @PathVariable titulo: String,
        @PathVariable username: String,
        authentication: Authentication
    ):ResponseEntity<Tarea>  {
        val tareaUpdate = tareaService.updateState(titulo, username, authentication)
        return ResponseEntity(tareaUpdate, HttpStatus.OK)
    }

    /**
     * Elimina una tarea específica.
     *
     * Este método recibe el título de la tarea y el nombre de usuario a través de la ruta,
     * y elimina la tarea. Verifica que el usuario autenticado tiene permisos para eliminar
     * tareas de otros usuarios si es necesario.
     *
     * @param titulo El título de la tarea que se desea eliminar.
     * @param username El nombre del usuario propietario de la tarea.
     * @param authentication El objeto de autenticación que contiene los detalles del usuario autenticado.
     * @return Una respuesta con un mensaje de éxito (Eliminado).
     * @throws BadRequestException Si el usuario no tiene permisos para eliminar la tarea de otro.
     * @throws NotFoundException Si la tarea no existe.
     */
    @DeleteMapping("/eliminarTarea/{titulo}/{username}")
    fun deleteTask(
        @PathVariable titulo: String,
        @PathVariable username: String,
        authentication: Authentication
    ):ResponseEntity<String>  {
        tareaService.deleteTask(titulo,username, authentication)
        return ResponseEntity("Eliminado", HttpStatus.OK)
    }
}