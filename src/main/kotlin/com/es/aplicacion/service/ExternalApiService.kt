package com.es.aplicacion.service

import com.es.aplicacion.domain.DatosMunicipios
import com.es.aplicacion.domain.DatosProvincias
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class ExternalApiService(private val webClient: WebClient.Builder) {


    fun obtenerDatosDesdeApi(): DatosProvincias? {
        return webClient.build()
            .get()
            .uri("https://apiv1.geoapi.es/provincias?type=JSON&key=7bf3b779377b08146d0b215aa43a9e49d7cb9153e7d3dc46b5503f2ba5f4ac6d")
            .retrieve()
            .bodyToMono(DatosProvincias::class.java)
            .block() // ⚠️ Esto bloquea el hilo, usar `subscribe()` en código reactivo
    }

    fun obtenerMunicipiosDesdeApi(cprom:String): DatosMunicipios? {
        return webClient.build()
            .get()
            .uri("https://apiv1.geoapi.es/municipios?CPRO=${cprom}&type=JSON&key=7bf3b779377b08146d0b215aa43a9e49d7cb9153e7d3dc46b5503f2ba5f4ac6d")
            .retrieve()
            .bodyToMono(DatosMunicipios::class.java)
            .block() // ⚠️ Esto bloquea el hilo, usar `subscribe()` en código reactivo
    }
}