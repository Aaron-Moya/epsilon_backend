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
@Table(name = "categorias")
public class Categorias implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String nombre;
	private Set<Productos> productos = new HashSet<>(0);

	public Categorias() {
	}

	public Categorias(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public Categorias(int id, String nombre, Set<Productos> productos) {
		this.id = id;
		this.nombre = nombre;
		this.productos = productos;
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

	@Column(name = "nombre", nullable = false)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "categorias")
	public Set<Productos> getProductos() {
		return this.productos;
	}

	public void setProductos(Set<Productos> productos) {
		this.productos = productos;
	}

}
