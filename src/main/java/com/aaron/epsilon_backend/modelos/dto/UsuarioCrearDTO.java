package com.aaron.epsilon_backend.modelos.dto;

import lombok.Data;

@Data
public class UsuarioCrearDTO {

	private String usuario;
	private String password;
	private String correo;
}
