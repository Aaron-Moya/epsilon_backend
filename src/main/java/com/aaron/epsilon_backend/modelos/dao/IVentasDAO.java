package com.aaron.epsilon_backend.modelos.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.entidades.Ventas;

public interface IVentasDAO extends CrudRepository<Ventas, Long> {

	List<Ventas> findByUsuarioVendedor(Usuarios usuario);
	List<Ventas> findByUsuarioComprador(Usuarios usuario);
}
