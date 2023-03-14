package com.aaron.epsilon_backend.modelos.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface IProductosDAO extends JpaRepository<Productos, Long> {

	public Page<Productos> findByUsuarios(Pageable page,Usuarios usuarios);
}
