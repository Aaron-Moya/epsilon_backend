package com.aaron.epsilon_backend.modelos.entidades;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "direcciones")
public class Direcciones implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String calle;
	private Integer numero;
	private String ciudad;
	private Set<Usuarios> usuarios = new HashSet<>(0);

	public Direcciones() {
	}

	public Direcciones(int id, String calle, String ciudad) {
		this.id = id;
		this.calle = calle;
		this.ciudad = ciudad;
	}

	public Direcciones(int id, String calle, Integer numero, String ciudad, Set<Usuarios> usuarios) {
		this.id = id;
		this.calle = calle;
		this.numero = numero;
		this.ciudad = ciudad;
		this.usuarios = usuarios;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "calle", nullable = false)
	public String getCalle() {
		return this.calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	@Column(name = "numero")
	public Integer getNumero() {
		return this.numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@Column(name = "ciudad", nullable = false)
	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "direccion")
	public Set<Usuarios> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Set<Usuarios> usuarios) {
		this.usuarios = usuarios;
	}

}
