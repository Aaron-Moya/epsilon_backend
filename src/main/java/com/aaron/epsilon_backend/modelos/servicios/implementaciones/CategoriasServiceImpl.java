package com.aaron.epsilon_backend.modelos.servicios.implementaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aaron.epsilon_backend.modelos.dao.ICategoriasDAO;
import com.aaron.epsilon_backend.modelos.entidades.Categorias;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.ICategoriasService;


@Service
public class CategoriasServiceImpl implements ICategoriasService {

	@Autowired
	private ICategoriasDAO categoriasDAO;
	
	@Override
	@Transactional(readOnly = true)
	public List<Categorias> findAll() {
		return (List<Categorias>) categoriasDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Categorias findById(Long id) {
		return categoriasDAO.findById(id).orElse(null);
	}

}
