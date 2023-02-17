package com.aaron.epsilon_backend.modelos.servicios.implementaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aaron.epsilon_backend.modelos.dao.IDireccionesDAO;
import com.aaron.epsilon_backend.modelos.entidades.Direcciones;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IDireccionesService;

@Service
public class DireccionesServiceImpl implements IDireccionesService {
	
	@Autowired
	private IDireccionesDAO direccionesDAO;

	@Override
	@Transactional(readOnly = true)
	public List<Direcciones> findAll() {
		return (List<Direcciones>) direccionesDAO.findAll();
	}

	@Override
	public Direcciones save(Direcciones direccion) {
		return direccionesDAO.save(direccion);
	}

	@Override
	@Transactional(readOnly = true)
	public Direcciones findById(Long id) {
		return direccionesDAO.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		direccionesDAO.delete(null);
	}

}
