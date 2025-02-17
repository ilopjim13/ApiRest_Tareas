package org.example.com.es.aplicacion.service

import org.example.com.es.aplicacion.repository.TareaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TareaService {

    @Autowired
    private lateinit var tareasRepository: TareaRepository

}