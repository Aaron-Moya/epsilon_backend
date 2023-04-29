package com.aaron.epsilon_backend.modelos.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.aaron.epsilon_backend.modelos.entidades.Cestas;
import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface ICestasDAO extends CrudRepository<Cestas, Long> {

	Cestas findByUsuarioAndProducto(Usuarios usuario, Productos producto);
	List<Cestas> findByUsuario(Usuarios usuario);
}
