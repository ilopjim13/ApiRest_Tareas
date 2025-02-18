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

@RestController
@RequestMapping("/tarea")
class TareaController {

    @Autowired
    private lateinit var tareaService: TareaService

    @GetMapping("/mostrar/{username}")
    fun getTask(
        @PathVariable username:String,
        authentication: Authentication
    ):ResponseEntity<List<Tarea>> {
        val tareas = tareaService.getTasks(username, authentication)
        return ResponseEntity(tareas, HttpStatus.OK)
    }

    @GetMapping("/mostrarTodas")
    fun getAll():ResponseEntity<List<Tarea>>  {
        return ResponseEntity(tareaService.getAllTasks(), HttpStatus.OK)
    }

    @PostMapping("/agregarTarea/{username}")
    fun addTask(
        @PathVariable username: String,
        @RequestBody tareaAdd: TareaAddDTO,
        authentication: Authentication
    ):ResponseEntity<Tarea>  {
        val tarea = tareaService.addTask(username, tareaAdd, authentication)
        return ResponseEntity(tarea, HttpStatus.OK)
    }

    @PostMapping("/actualizarEstado/{titulo}/{username}")
    fun updateState(
        @PathVariable titulo: String,
        @PathVariable username: String,
        authentication: Authentication
    ):ResponseEntity<Tarea>  {
        val tareaUpdate = tareaService.updateState(titulo, username, authentication)
        return ResponseEntity(tareaUpdate, HttpStatus.OK)
    }

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