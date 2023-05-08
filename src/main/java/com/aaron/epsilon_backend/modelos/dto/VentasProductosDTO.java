package com.aaron.epsilon_backend.modelos.dto;

import java.util.Date;

import com.aaron.epsilon_backend.modelos.entidades.Productos;

import lombok.Data;

@Data
public class VentasProductosDTO {

	private int id;
	private int idVenta;
	private Date fechaVenta;
	private ProductoDTO producto;
	private int cantidad;
	private float total;
}
