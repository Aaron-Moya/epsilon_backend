package com.aaron.epsilon_backend.modelos.servicios.implementaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aaron.epsilon_backend.modelos.dao.ICestasDAO;
import com.aaron.epsilon_backend.modelos.entidades.Cestas;
import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.ICestasService;

@Service
public class CestasServiceImpl implements ICestasService {

	@Autowired
	private ICestasDAO cestasDAO;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cestas> findAll() {
		return (List<Cestas>) cestasDAO.findAll();
	}

	@Override
	public Cestas save(Cestas cesta) {
		return cestasDAO.save(cesta);
	}

	@Override
	@Transactional(readOnly = true)
	public Cestas findById(Long id) {
		return cestasDAO.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		cestasDAO.deleteById(id);
		
	}

	@Override
	public Cestas findByUsuarioAndProducto(Usuarios usuario, Productos producto) {
		return cestasDAO.findByUsuarioAndProducto(usuario, producto);
	}

}
