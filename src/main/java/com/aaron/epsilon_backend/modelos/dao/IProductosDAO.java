package com.aaron.epsilon_backend.modelos.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.utilidades.FiltroCategoria;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface IProductosDAO extends JpaRepository<Productos, Long> {

	@Query(value = "SELECT * FROM PRODUCTOS WHERE BORRADO = false AND STOCK > 0", nativeQuery = true)
	Page<Productos> findAll(Pageable page);
	
	@Query(value = "select productos.* from productos, usuarios where productos.id_usuario = usuarios.id "
			+ "and productos.borrado = false and productos.id_usuario = :#{#usuarios.id}",
			nativeQuery = true)
	Page<Productos> findByUsuarios(Pageable page,Usuarios usuarios);
	
	@Query(value = "SELECT producto "
			+ "FROM Productos producto "
			+ "WHERE borrado = false "
			+ "AND (:#{#filtro.nombre} IS NULL OR LOWER(producto.nombre) LIKE '%' || LOWER(:#{#filtro.nombre}) || '%') "
			+ "AND (:#{#filtro.estado} IS NULL OR LOWER(producto.estado) = LOWER(:#{#filtro.estado})) "
			+ "AND (:#{#filtro.stock} IS NULL OR producto.stock >= :#{#filtro.stock}) "
			+ "AND (:#{#filtro.precio} IS NULL OR producto.precio <= :#{#filtro.precio}) "
			+ "AND (:#{#filtro.categoria} IS NULL OR LOWER(producto.categorias.nombre) = LOWER(:#{#filtro.categoria})) "
		)
	Page<Productos> getByFiltroCategorias(FiltroCategoria filtro, Pageable page);
	//public Page<Productos> findByUsuariosAndUsuariosCesta(Pageable page,Usuarios usuarios);
}
