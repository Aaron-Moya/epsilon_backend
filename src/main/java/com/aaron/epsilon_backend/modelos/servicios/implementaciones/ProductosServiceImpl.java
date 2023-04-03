package com.aaron.epsilon_backend.modelos.servicios.implementaciones;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aaron.epsilon_backend.modelos.dao.IProductosDAO;
import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IProductosService;
import com.aaron.epsilon_backend.utilidades.FiltroCategoria;

@Service
public class ProductosServiceImpl implements IProductosService {

	@Autowired
	private IProductosDAO productosDAO;
	
	@Override
	public Page<Productos> findAll(Pageable page) {
		return productosDAO.findAll(page);
	}

	@Override
	public Productos save(Productos producto) {
		return productosDAO.save(producto);
	}

	@Override
	public Productos findById(Long id) {
		return productosDAO.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<Productos> findByUsuarios(Pageable page, Usuarios usuarios) {
		return productosDAO.findByUsuarios(page, usuarios);
	}

	@Override
	public Page<Productos> findByFiltroCategoria(FiltroCategoria filtro, Pageable page) {
		return productosDAO.getByFiltroCategorias(filtro, page);
	}

}
