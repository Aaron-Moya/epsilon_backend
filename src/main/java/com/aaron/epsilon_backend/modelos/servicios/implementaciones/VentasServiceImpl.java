package com.aaron.epsilon_backend.modelos.servicios.implementaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aaron.epsilon_backend.modelos.dao.IVentasDAO;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.entidades.Ventas;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IVentasService;

@Service
public class VentasServiceImpl implements IVentasService {

	@Autowired
	private IVentasDAO ventasDAO;
	
	@Override
	public List<Ventas> findAll() {
		return (List<Ventas>) ventasDAO.findAll();
	}

	@Override
	public Ventas findById(Long id) {
		return ventasDAO.findById(id).orElse(null);
	}

	@Override
	public List<Ventas> findByUsuarioComprador(Usuarios usuario) {
		return ventasDAO.findByUsuarioComprador(usuario);
	}

	@Override
	public Ventas save(Ventas venta) {
		return ventasDAO.save(venta);
	}

}
