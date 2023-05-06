package com.aaron.epsilon_backend.modelos.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.aaron.epsilon_backend.modelos.entidades.Ventas;
import com.aaron.epsilon_backend.modelos.entidades.VentasProductos;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface IVentasProductosDAO extends CrudRepository<VentasProductos, Long> {

	List<VentasProductos> findByVenta(Ventas venta);
}
