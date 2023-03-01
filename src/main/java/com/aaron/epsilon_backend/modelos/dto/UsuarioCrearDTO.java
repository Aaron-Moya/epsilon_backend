package com.aaron.epsilon_backend.modelos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class UsuarioCrearDTO {

	private String usuario;
	private String password;
	private String correo;
}
