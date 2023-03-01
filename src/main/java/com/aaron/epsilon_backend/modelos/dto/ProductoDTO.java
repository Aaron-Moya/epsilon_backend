package com.aaron.epsilon_backend.modelos.dto;

import lombok.Data;

@Data
public class ProductoDTO {

	private int id;
	private String nombre;
	private float precio;
	private String estado;
	private String imagen;
}
