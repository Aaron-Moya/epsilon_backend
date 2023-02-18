package com.aaron.epsilon_backend.modelos.servicios.interfaces;

import java.util.List;

import com.aaron.epsilon_backend.modelos.entidades.Usuarios;

public interface IUsuariosService {

	public List<Usuarios> findAll();
	
	public Usuarios findById(Long id);

	public Usuarios save(Usuarios usuario);
	
	void delete(Long id);
	
}
