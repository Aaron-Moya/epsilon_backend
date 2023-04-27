package com.aaron.epsilon_backend.modelos.servicios.interfaces;

import java.util.List;

import com.aaron.epsilon_backend.modelos.entidades.Cestas;
import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;

public interface ICestasService {

	public List<Cestas> findAll();
	
	public Cestas findByUsuarioAndProducto(Usuarios usuario, Productos producto);
	
	public Cestas save(Cestas cesta);
	
	public Cestas findById(Long id);
	
	void delete(Long id);
}
