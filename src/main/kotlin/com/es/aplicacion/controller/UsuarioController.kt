package com.es.aplicacion.controller

import com.es.aplicacion.dto.LoginUsuarioDTO
import com.es.aplicacion.dto.UsuarioDTO
import com.es.aplicacion.dto.UsuarioRegisterDTO
import com.es.aplicacion.error.exception.UnauthorizedException
import com.es.aplicacion.service.TokenService
import com.es.aplicacion.service.UsuarioService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controlador REST para gestionar usuarios en el sistema.
 * Permite realizar operaciones como el registro, login y obtener información del usuario autenticado.
 */
@RestController
@RequestMapping("/usuarios")
class UsuarioController {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager
    @Autowired
    private lateinit var tokenService: TokenService
    @Autowired
    private lateinit var usuarioService: UsuarioService

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * Recibe un objeto `usuarioRegisterDTO` en el cuerpo de la solicitud y lo procesa para insertar
     * un nuevo usuario en la base de datos. Si la inserción es exitosa, devuelve una respuesta con
     * el objeto `usuarioDTO` del usuario recién creado.
     *
     * @param httpRequest La solicitud HTTP.
     * @param usuarioRegisterDTO Objeto que contiene los datos del usuario a registrar.
     * @return Una respuesta con el objeto `usuarioDTO` del usuario creado y un estado `HttpStatus.CREATED`.
     * @throws BadRequestException Si hay errores en los datos proporcionados.
     */
    @PostMapping("/register")
    fun insert(
        httpRequest: HttpServletRequest,
        @RequestBody usuarioRegisterDTO: UsuarioRegisterDTO
    ) : ResponseEntity<UsuarioDTO>?{
        val usuarioDTO = usuarioService.insertUser(usuarioRegisterDTO)
        return ResponseEntity(usuarioDTO, HttpStatus.CREATED)
    }


    /**
     * Realiza el login de un usuario en el sistema.
     *
     * Recibe un objeto `LoginUsuarioDTO` con las credenciales de login (usuario y contraseña),
     * y usa el `AuthenticationManager` para autenticar al usuario. Si la autenticación es exitosa,
     * se genera un token JWT para el usuario autenticado y se devuelve en la respuesta.
     *
     * @param usuario Objeto que contiene las credenciales del usuario (usuario y contraseña).
     * @return Una respuesta con un mapa que contiene el token generado.
     * @throws UnauthorizedException Si las credenciales proporcionadas son incorrectas.
     */
    @PostMapping("/login")
    fun login(@RequestBody usuario: LoginUsuarioDTO) : ResponseEntity<Any>? {

        val authentication: Authentication
        try {
            authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(usuario.username, usuario.password))
        } catch (e: AuthenticationException) {
            throw UnauthorizedException("Credenciales user: ${usuario.username}, pass: ${usuario.password} incorrectas")
        }

        val token = tokenService.generarToken(authentication)

        return ResponseEntity(mapOf("token" to token), HttpStatus.CREATED)
    }


    /**
     * Obtiene los datos del usuario autenticado.
     *
     * Devuelve los datos del usuario que está actualmente autenticado. El objeto `Authentication`
     * contiene la información del usuario autenticado.
     *
     * @param authentication El objeto de autenticación que contiene el usuario autenticado.
     * @return Una respuesta con el objeto `usuarioDTO` del usuario autenticado y un estado `HttpStatus.OK`.
     */
    @GetMapping("/usuario")
    fun getUser(
        authentication: Authentication
    ):ResponseEntity<UsuarioDTO> {
        val usuarioDTO = usuarioService.getUser(authentication)
        return ResponseEntity<UsuarioDTO>(usuarioDTO, HttpStatus.OK)
    }

    /**
     * Obtiene todos los usuarios de la base de datos y los devuelve
     *
     * @param authentication El objeto de autenticación que contiene el usuario autenticado.
     * @return Una respuesta con el objeto `List<UsuarioDTO>` de todos los usuarios y un estado `HttpStatus.OK`.
     */
    @GetMapping("/usuarios")
    fun getUsers(
        authentication: Authentication
    ):ResponseEntity<List<UsuarioDTO>> {
        val usuarioDTO = usuarioService.getUsers()
        return ResponseEntity<List<UsuarioDTO>>(usuarioDTO, HttpStatus.OK)
    }

}