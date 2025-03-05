package com.es.aplicacion.service

import com.es.aplicacion.domain.DatosMunicipios
import com.es.aplicacion.dto.UsuarioDTO
import com.es.aplicacion.dto.UsuarioRegisterDTO
import com.es.aplicacion.error.exception.BadRequestException
import com.es.aplicacion.error.exception.NotFoundException
import com.es.aplicacion.error.exception.UnauthorizedException
import com.es.aplicacion.model.Usuario
import com.es.aplicacion.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class UsuarioService : UserDetailsService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var apiService: ExternalApiService

    /**
     * Carga un usuario por su nombre de usuario (username) para la autenticación.
     *
     * @param username Nombre de usuario a buscar.
     * @return UserDetails con la información del usuario.
     * @throws UnauthorizedException si el usuario no existe.
     */
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

    /**
     * Registra un nuevo usuario en la base de datos, validando sus datos y su dirección a través de una API externa.
     *
     * @param usuarioInsertadoDTO DTO con los datos del usuario a registrar.
     * @return UsuarioDTO con los datos del usuario registrado.
     * @throws NotFoundException si la provincia o el municipio no son válidos.
     * @throws BadRequestException si los datos son inválidos o el usuario ya existe.
     */
    fun insertUser(usuarioInsertadoDTO: UsuarioRegisterDTO) : UsuarioDTO? {
        // Obtiene los datos de provincias desde una API externa
        val datosProvincias = apiService.obtenerDatosDesdeApi()
        var datosMunicipios: DatosMunicipios? = null

        // Validación de la provincia
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

        // Validación del municipio
        if (datosMunicipios != null) {
            if(datosMunicipios.data != null) {
                datosMunicipios.data!!.stream().filter {
                    it.DMUN50 == usuarioInsertadoDTO.direccion.municipio.uppercase()
                }.findFirst().orElseThrow {
                    NotFoundException("Municipio ${usuarioInsertadoDTO.direccion.municipio.uppercase()} no válido")
                }
            }
        }

        // Validaciones de datos básicos
        if (usuarioInsertadoDTO.password.isBlank() || usuarioInsertadoDTO.username.isBlank() || usuarioInsertadoDTO.email.isBlank()) throw BadRequestException("No puede estar la contraseña vacía")

        // Verifica si el usuario ya existe en la base de datos
        val usuarioBD: Usuario? = usuarioRepository.findByUsername(usuarioInsertadoDTO.username).getOrNull()
        if (usuarioBD != null) throw BadRequestException("Este usuario ya existe")

        // Validación de contraseñas y rol
        if (usuarioInsertadoDTO.password == usuarioInsertadoDTO.passwordRepeat) {
            if(usuarioInsertadoDTO.rol != "USER" && usuarioInsertadoDTO.rol != "ADMIN") throw BadRequestException("No tiene rol")
            // Creación del nuevo usuario
            val usuario = Usuario(null, usuarioInsertadoDTO.username, passwordEncoder.encode(usuarioInsertadoDTO.password), usuarioInsertadoDTO.email, usuarioInsertadoDTO.rol , usuarioInsertadoDTO.direccion)
            usuarioRepository.insert(usuario)
            return UsuarioDTO(usuario.username, usuario.email, usuario.roles)

        } else throw BadRequestException("La contraseña debe ser igual en los dos casos")
    }

    /**
     * Obtiene los datos del usuario autenticado.
     *
     * @param authentication Información de autenticación del usuario.
     * @return UsuarioDTO con los datos del usuario autenticado.
     * @throws NotFoundException si el usuario no existe.
     */
    fun getUser(authentication: Authentication):UsuarioDTO {
        val usuarioBD =  usuarioRepository.findByUsername(authentication.name).getOrElse { throw  NotFoundException("Este usuario no existe") }
        val usuarioDTO = UsuarioDTO(usuarioBD.username, usuarioBD.email, usuarioBD.roles)

        return usuarioDTO
    }

    /**
     * Obtiene los datos de todos los usuairos.
     *
     * @return UsuarioDTO con los datos del usuario autenticado.
     */
    fun getUsers():MutableList<UsuarioDTO> {
        val usuariosBD =  usuarioRepository.findAll()
        val listaUsuarios = mutableListOf<UsuarioDTO>()
        usuariosBD.forEach {
            val usuarioDTO = UsuarioDTO(it.username, it.email, it.roles)
            listaUsuarios.add(usuarioDTO)
        }

        return listaUsuarios
    }
}