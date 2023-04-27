package com.aaron.epsilon_backend.modelos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class CestaDTO {

	private int id;
	private int idUsuario;
	private int idProducto;
	private Integer cantidad;
}
