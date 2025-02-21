package com.es.aplicacion.repository

import com.es.aplicacion.model.Tarea

interface TareaCustomRepository {

    /**
     * Obtiene todas las tareas de un usuario específico.
     *
     * @param username Nombre de usuario para filtrar las tareas.
     * @return Una lista de tareas asociadas al usuario.
     * @throws NoSuchElementException si no se encuentran tareas para el usuario.
     */
    fun findAllByUsername(username:String) :List<Tarea>

    /**
     * Busca una tarea por título y nombre de usuario.
     *
     * @param titulo Título de la tarea que se busca.
     * @param username Nombre de usuario para asegurarse de que la tarea pertenece a este usuario.
     * @return La tarea encontrada o `null` si no se encuentra ninguna coincidencia.
     * @throws NoSuchElementException si no se encuentra una tarea con el título y usuario proporcionados.
     */
    fun findByTituloUsername(titulo:String, username: String): Tarea?
}