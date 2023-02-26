package com.aaron.epsilon_backend.auth.dto;
import java.io.Serializable;
import java.util.Date;

import com.aaron.epsilon_backend.modelos.entidades.Direcciones;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class RespuestaTokenDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private DireccionDto direccion;
	private String usuario;
	private String correo;
	private Date fechaCreacion;
	private String avatar;
	private String accessToken;
}