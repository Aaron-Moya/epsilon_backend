package com.aaron.epsilon_backend.modelos.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductoDTO {

	private int id;
	private String nombre;
	private String descripcion;
	private Date fechaCreacion;
	private int stock;
	private float precio;
	private String estado;
	private String imagen;
}
