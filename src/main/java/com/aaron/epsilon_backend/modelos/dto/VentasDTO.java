package com.aaron.epsilon_backend.modelos.dto;

import java.util.Date;

import com.aaron.epsilon_backend.modelos.entidades.Usuarios;

import lombok.Data;

@Data
public class VentasDTO {

	private int id;
	private Long usuarioComprador;
	private Date fechaVenta;
	private float total;
	private int totalProductos;
}
