package com.aaron.epsilon_backend.modelos.servicios.interfaces;

import java.util.List;

import com.aaron.epsilon_backend.modelos.entidades.Productos;

public interface IProductosService {

	public List<Productos> findAll();
	
	public Productos save(Productos producto);
	
	public Productos findById(Long id);
	
	void delete(Long id);
}
