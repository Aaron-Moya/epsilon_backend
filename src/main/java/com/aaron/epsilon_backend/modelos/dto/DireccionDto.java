package com.aaron.epsilon_backend.modelos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class DireccionDto {

	private int id;
	private String calle;
	private Integer numero;
	private String ciudad;
}
