package com.aaron.epsilon_backend.modelos.dao;

import org.springframework.data.repository.CrudRepository;

import com.aaron.epsilon_backend.modelos.entidades.Usuarios;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface IUsuariosDAO extends CrudRepository<Usuarios, Long> {

}
