package com.aaron.epsilon_backend.modelos.servicios.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.aaron.epsilon_backend.modelos.entidades.Productos;

public interface IProductosService {

	public Page<Productos> findAll(Pageable page);
	
	public Productos save(Productos producto);
	
	public Productos findById(Long id);
	
	void delete(Long id);
}
