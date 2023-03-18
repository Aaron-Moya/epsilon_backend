package com.aaron.epsilon_backend.modelos.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface IProductosDAO extends JpaRepository<Productos, Long> {

	@Query(value = "SELECT * FROM PRODUCTOS WHERE BORRADO = false", nativeQuery = true)
	public Page<Productos> findAll(Pageable page);
	
	@Query(value = "select productos.* from productos, usuarios where productos.id_usuario = usuarios.id "
			+ "and productos.borrado = false and productos.id_usuario = :#{#usuarios.id}",
			nativeQuery = true)
	public Page<Productos> findByUsuarios(Pageable page,Usuarios usuarios);
	//public Page<Productos> findByUsuariosAndUsuariosCesta(Pageable page,Usuarios usuarios);
}
