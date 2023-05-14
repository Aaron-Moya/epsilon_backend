package com.aaron.epsilon_backend.modelos.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.entidades.Ventas;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface IVentasDAO extends CrudRepository<Ventas, Long> {

	List<Ventas> findByUsuarioComprador(Usuarios usuario);
}
