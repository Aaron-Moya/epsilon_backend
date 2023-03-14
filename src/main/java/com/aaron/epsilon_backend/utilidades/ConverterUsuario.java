package com.aaron.epsilon_backend.utilidades;

import java.util.Date;
import java.util.Objects;

import com.aaron.epsilon_backend.modelos.dto.UsuarioDTO;
import com.aaron.epsilon_backend.modelos.dto.UsuarioLoginDTO;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class ConverterUsuario {

	public static UsuarioLoginDTO convertirUsuarioLogin(Usuarios user) {
		Algorithm algorithm = Algorithm.HMAC256("token101");
        UsuarioLoginDTO respuestaTokenDto = new UsuarioLoginDTO();
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
	
	public static UsuarioDTO convertirUsuario(Usuarios user) {
		UsuarioDTO usuarioDto = new UsuarioDTO();
		usuarioDto.setId(user.getId());
		usuarioDto.setCorreo(user.getCorreo());
		usuarioDto.setUsuario(user.getUsuario());
		usuarioDto.setFechaCreacion(user.getFechaCreacion());
		usuarioDto.setAvatar(user.getAvatar());
        if (Objects.nonNull(user.getDireccion()))
        	usuarioDto.setDireccion(ConverterDirecciones.convertirDireccion(user.getDireccion()));
        
        return usuarioDto;
	}
	
	public static Usuarios convertirUsuarioDTO(UsuarioDTO userDTO) {
		Usuarios usuario = new Usuarios();
		usuario.setId(userDTO.getId());
		usuario.setCorreo(userDTO.getCorreo());
		usuario.setUsuario(userDTO.getUsuario());
		usuario.setFechaCreacion(userDTO.getFechaCreacion());
		usuario.setAvatar(userDTO.getAvatar());
        if (Objects.nonNull(userDTO.getDireccion()))
        	usuario.setDireccion(ConverterDirecciones.convertirDireccionDTO(userDTO.getDireccion()));
        
        return usuario;
	}
}
