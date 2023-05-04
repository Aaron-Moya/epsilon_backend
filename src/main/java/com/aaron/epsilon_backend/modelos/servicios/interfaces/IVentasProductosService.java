package com.aaron.epsilon_backend.modelos.servicios.interfaces;

import java.util.List;

import com.aaron.epsilon_backend.modelos.entidades.VentasProductos;

public interface IVentasProductosService {

	public List<VentasProductos> findAll();
	
	public VentasProductos findById(Long id);
	
	public VentasProductos save(VentasProductos venta);
	
}
