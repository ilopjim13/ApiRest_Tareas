package com.es.aplicacion.service

import com.es.aplicacion.domain.DatosMunicipios
import com.es.aplicacion.dto.UsuarioDTO
import com.es.aplicacion.dto.UsuarioRegisterDTO
import aplicacion.error.exception.BadRequestException
import aplicacion.error.exception.NotFoundException
import aplicacion.error.exception.UnauthorizedException
import com.es.aplicacion.model.Usuario
import com.es.aplicacion.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UsuarioService : UserDetailsService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var apiService: ExternalApiService


    override fun loadUserByUsername(username: String?): UserDetails {
        val usuario: Usuario = usuarioRepository
            .findByUsername(username!!)
            .orElseThrow {
                UnauthorizedException("$username no existente")
            }

        return User.builder()
            .username(usuario.username)
            .password(usuario.password)
            .roles(usuario.roles)
            .build()
    }

    fun insertUser(usuarioInsertadoDTO: UsuarioRegisterDTO) : UsuarioDTO? {

        val datosProvincias = apiService.obtenerDatosDesdeApi()
        var datosMunicipios: DatosMunicipios? = null


        // Si los datos vienen rellenos entonces busco la provincia dentro del resultado de la llamada
        if (datosProvincias != null) {
            if(datosProvincias.data != null) {
                val provincia = datosProvincias.data.stream().filter {
                    it.PRO == usuarioInsertadoDTO.direccion.provincia.uppercase()
                }.findFirst().orElseThrow {
                    NotFoundException("Provincia ${usuarioInsertadoDTO.direccion.provincia.uppercase()} no válida")
                }
                datosMunicipios = apiService.obtenerMunicipiosDesdeApi(provincia.CPRO)
            }
        }

        if (datosMunicipios != null) {
            if(datosMunicipios.data != null) {
                datosMunicipios.data!!.stream().filter {
                    it.DMUN50 == usuarioInsertadoDTO.direccion.municipio.uppercase()
                }.findFirst().orElseThrow {
                    NotFoundException("Municipio ${usuarioInsertadoDTO.direccion.municipio.uppercase()} no válido")
                }
            }
        }

        if (usuarioInsertadoDTO.password.isBlank() || usuarioInsertadoDTO.username.isBlank() || usuarioInsertadoDTO.email.isBlank()) throw BadRequestException("No puede estar la contraseña vacía")

        val usuarioBD: Usuario? = usuarioRepository.findByUsername(usuarioInsertadoDTO.username).getOrNull()

        if (usuarioBD != null) throw BadRequestException("Este usuario ya existe")

        if (usuarioInsertadoDTO.password == usuarioInsertadoDTO.passwordRepeat) {

            if(usuarioInsertadoDTO.rol != "USER" && usuarioInsertadoDTO.rol != "ADMIN") throw BadRequestException("No tiene rol")

            val usuario = Usuario(null, usuarioInsertadoDTO.username, passwordEncoder.encode(usuarioInsertadoDTO.password), usuarioInsertadoDTO.email, usuarioInsertadoDTO.rol , usuarioInsertadoDTO.direccion)
            usuarioRepository.insert(usuario)
            return UsuarioDTO(usuario.username, usuario.email, usuario.roles)


        } else throw BadRequestException("La contraseña debe ser igual en los dos casos")

    }
}