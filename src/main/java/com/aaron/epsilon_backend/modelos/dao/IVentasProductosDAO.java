package com.aaron.epsilon_backend.modelos.dao;

import org.springframework.data.repository.CrudRepository;

import com.aaron.epsilon_backend.modelos.entidades.VentasProductos;

public interface IVentasProductosDAO extends CrudRepository<VentasProductos, Long> {

}
