package com.aaron.epsilon_backend.modelos.dao;

import org.springframework.data.repository.CrudRepository;

import com.aaron.epsilon_backend.modelos.entidades.Productos;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface IProductosDAO extends CrudRepository<Productos, Long> {

}
