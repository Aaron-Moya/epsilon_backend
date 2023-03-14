package com.aaron.epsilon_backend.modelos.servicios.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;

public interface IProductosService {

	public Page<Productos> findAll(Pageable page);
	
	public Productos save(Productos producto);
	
	public Productos findById(Long id);
	
	public Page<Productos> findByUsuarios(Pageable page, Usuarios usuarios);
	
	void delete(Long id);
}
