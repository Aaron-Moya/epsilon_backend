package com.aaron.epsilon_backend.modelos.servicios.interfaces;

import java.util.List;

import com.aaron.epsilon_backend.modelos.entidades.Categorias;

public interface ICategoriasService {

	public List<Categorias> findAll();
	
	public Categorias findById(Long id);
}
