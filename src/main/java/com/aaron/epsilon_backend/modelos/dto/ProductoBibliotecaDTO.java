package com.aaron.epsilon_backend.modelos.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductoBibliotecaDTO {

	private int id;
	private String nombre;
	private String descripcion;
	private Date fechaCreacion;
	private int stock;
	private float precio;
	private float descuento;
	private String estado;
	private String imagen;
	private boolean borrado;
	private CategoriaDTO categorias;
	private UsuarioDTO usuarios;
	private int cantidad;
}
