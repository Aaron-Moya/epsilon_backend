package com.aaron.epsilon_backend.modelos.servicios.implementaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aaron.epsilon_backend.modelos.dao.IVentasProductosDAO;
import com.aaron.epsilon_backend.modelos.entidades.Ventas;
import com.aaron.epsilon_backend.modelos.entidades.VentasProductos;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IVentasProductosService;

@Service
public class VentasProductosServiceImpl implements IVentasProductosService {

	@Autowired
	private IVentasProductosDAO ventasProductosDAO;
	
	@Override
	public List<VentasProductos> findAll() {
		return (List<VentasProductos>) ventasProductosDAO.findAll();
	}

	@Override
	public VentasProductos findById(Long id) {
		return ventasProductosDAO.findById(id).orElse(null);
	}

	@Override
	public VentasProductos save(VentasProductos venta) {
		return ventasProductosDAO.save(venta);
	}

	@Override
	public List<VentasProductos> findByVenta(Ventas venta) {
		return ventasProductosDAO.findByVenta(venta);
	}

}
