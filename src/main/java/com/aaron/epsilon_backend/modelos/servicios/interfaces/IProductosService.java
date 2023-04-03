package com.aaron.epsilon_backend.modelos.servicios.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.utilidades.FiltroCategoria;

public interface IProductosService {

	public Page<Productos> findAll(Pageable page);
	
	public Page<Productos> findByFiltroCategoria(@Param("filtro") FiltroCategoria filtro, Pageable page);
	
	public Productos save(Productos producto);
	
	public Productos findById(Long id);
	
	public Page<Productos> findByUsuarios(Pageable page, Usuarios usuarios);
	
	void delete(Long id);
}
