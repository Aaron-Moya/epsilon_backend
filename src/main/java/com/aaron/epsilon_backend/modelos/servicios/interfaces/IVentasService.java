package com.aaron.epsilon_backend.modelos.servicios.interfaces;

import java.util.List;

import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.entidades.Ventas;

public interface IVentasService {

	public List<Ventas> findAll();
	
	public Ventas findById(Long id);
	
	public List<Ventas> findByUsuarioVendedor(Usuarios usuario);
	
	public List<Ventas> findByUsuarioComprador(Usuarios usuario);
	
	public Ventas save(Ventas venta);
	
}
