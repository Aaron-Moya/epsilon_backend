package com.aaron.epsilon_backend.modelos.servicios.interfaces;

import java.util.List;

import com.aaron.epsilon_backend.modelos.entidades.Direcciones;

public interface IDireccionesService {

	public List<Direcciones> findAll();
	
	public Direcciones save(Direcciones direccion);
	
	public Direcciones findById(Long id);
	
	void delete(Long id);
}
