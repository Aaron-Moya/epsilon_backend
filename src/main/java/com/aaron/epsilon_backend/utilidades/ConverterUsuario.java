package com.aaron.epsilon_backend.utilidades;

import java.util.Date;
import java.util.Objects;

import com.aaron.epsilon_backend.auth.dto.RespuestaTokenDto;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class ConverterUsuario {

	public static RespuestaTokenDto convertirUsuario(Usuarios user) {
		Algorithm algorithm = Algorithm.HMAC256("token101");
        RespuestaTokenDto respuestaTokenDto = new RespuestaTokenDto();
        respuestaTokenDto.setId(user.getId());
        respuestaTokenDto.setCorreo(user.getCorreo());
        respuestaTokenDto.setUsuario(user.getUsuario());
        respuestaTokenDto.setFechaCreacion(user.getFechaCreacion());
        respuestaTokenDto.setAvatar(user.getAvatar());
        if (Objects.nonNull(user.getDireccion()))
        	respuestaTokenDto.setDireccion(ConverterDirecciones.convertirDireccion(user.getDireccion()));
        String token = JWT.create()
                .withIssuer("frangarcia")
                .withClaim("id", user.getId())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000))) // Caduca en un día
                .sign(algorithm);
        respuestaTokenDto.setAccessToken(token);
        
        return respuestaTokenDto;
	}
}
