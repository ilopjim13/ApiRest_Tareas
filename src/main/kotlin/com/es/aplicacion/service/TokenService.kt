package com.es.aplicacion.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class TokenService {

    @Autowired
    private lateinit var jwtEncoder: JwtEncoder

    /**
     * Genera un token JWT basado en la información del usuario autenticado.
     *
     * @param authentication Información de autenticación del usuario.
     * @return Un token JWT como `String`.
     */
    fun generarToken(authentication: Authentication) : String {
        // Obtiene los roles del usuario y los convierte en una cadena separada por espacios
        val roles: String = authentication.authorities.joinToString(" ") { it.authority } // Contiene los roles del usuario
        // Construcción del payload del token JWT
        val payload: JwtClaimsSet = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(Instant.now())
            .expiresAt(Date().toInstant().plus(Duration.ofHours(1)))
            .subject(authentication.name)
            .claim("roles", roles)
            .build()

        return jwtEncoder.encode(JwtEncoderParameters.from(payload)).tokenValue
    }
}