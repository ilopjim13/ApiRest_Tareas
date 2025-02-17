package org.example.com.es.aplicacion.controller

import org.example.com.es.aplicacion.model.Tarea
import org.example.com.es.aplicacion.service.TareaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tareas")
class TareaController {

    @Autowired
    private lateinit var tareaService: TareaService

    @GetMapping("/mostrar/{username}")
    fun getTask(
        @PathVariable username:String
    ):ResponseEntity<Tarea> {

    }

    @GetMapping("/mostrarTodas")
    fun getAll():ResponseEntity<List<Tarea>>  {

    }

    @PostMapping("/agregarTarea/{username}")
    fun addTask(
        @PathVariable username: String
    ):ResponseEntity<Tarea>  {

    }

    @PostMapping("/actualizarEstado/{username}")
    fun updateState(
        @PathVariable username: String
    ):ResponseEntity<Tarea>  {

    }

    @DeleteMapping("/eliminarTarea/{username}")
    fun deleteTask(
        @PathVariable username: String
    ):ResponseEntity<Tarea>  {

    }

}