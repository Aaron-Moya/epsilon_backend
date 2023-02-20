package com.aaron.epsilon_backend.modelos.servicios.implementaciones;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aaron.epsilon_backend.modelos.dao.IUsuariosDAO;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IUsuariosService;

@Service
public class UsuariosServiceImpl implements IUsuariosService {

	@Autowired
	private IUsuariosDAO usuariosDAO;
	
	@Override
	@Transactional(readOnly = true)
	public List<Usuarios> findAll() {
		return (List<Usuarios>) usuariosDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Usuarios findById(Long id) {
		return usuariosDAO.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Usuarios findByCorreo(String correo) {
		return usuariosDAO.findByCorreo(correo);
	}

	@Override
	public Usuarios save(Usuarios usuario) {
		return usuariosDAO.save(usuario);
	}
	
	@Override
	public void delete(Long id) {
		usuariosDAO.delete(null);
	}

}
