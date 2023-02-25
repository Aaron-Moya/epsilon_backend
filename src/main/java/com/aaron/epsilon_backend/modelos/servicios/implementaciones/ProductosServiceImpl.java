package com.aaron.epsilon_backend.modelos.servicios.implementaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aaron.epsilon_backend.modelos.dao.IProductosDAO;
import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IProductosService;

@Service
public class ProductosServiceImpl implements IProductosService {

	@Autowired
	private IProductosDAO productosDAO;
	
	@Override
	public List<Productos> findAll() {
		return (List<Productos>) productosDAO.findAll();
	}

	@Override
	public Productos save(Productos producto) {
		return productosDAO.save(producto);
	}

	@Override
	public Productos findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}
