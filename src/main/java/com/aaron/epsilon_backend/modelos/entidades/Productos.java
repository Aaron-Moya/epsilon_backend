package com.aaron.epsilon_backend.modelos.entidades;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Productos generated by hbm2java
 */
@Entity
@Table(name = "productos")
public class Productos implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private Categorias categorias;
	private Usuarios usuarios;
	private String nombre;
	private String descripcion;
	private float precio;
	private int stock;
	private String estado;
	private Date fechaCreacion;
	private String imagen;
	private boolean borrado;
	private Set<Usuarios> usuariosCesta = new HashSet<>(0);
	private Set<VentasProductos> ventasProductos = new HashSet<>(0);
	private Set<Usuarios> usuariosFavorito = new HashSet<>(0);

	public Productos() {
	}

	public Productos(int id, Categorias categorias, Usuarios usuarios, String nombre, String descripcion, float precio, int stock,
			String estado, Date fechaCreacion, String imagen, boolean borrado) {
		this.id = id;
		this.categorias = categorias;
		this.usuarios = usuarios;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.estado = estado;
		this.fechaCreacion = fechaCreacion;
		this.imagen = imagen;
		this.borrado = borrado;
	}

	public Productos(int id, Categorias categorias, Usuarios usuarios, String nombre, String descripcion, float precio, int stock,
			String estado, Date fechaCreacion, String imagen, boolean borrado, 
			Set<Usuarios> usuariosCesta, Set<VentasProductos> ventasProductos, Set<Usuarios> usuariosFavorito) {
		this.id = id;
		this.categorias = categorias;
		this.usuarios = usuarios;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.estado = estado;
		this.fechaCreacion = fechaCreacion;
		this.imagen = imagen;
		this.borrado = borrado;
		this.usuariosCesta = usuariosCesta;
		this.ventasProductos = ventasProductos;
		this.usuariosFavorito = usuariosFavorito;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_categoria", nullable = false)
	@JsonIgnoreProperties("productos")
	public Categorias getCategorias() {
		return this.categorias;
	}

	public void setCategorias(Categorias categorias) {
		this.categorias = categorias;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = false)
	@JsonIgnoreProperties("productos")
	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	@Column(name = "nombre", nullable = false)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "descripcion", nullable = false)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "precio", nullable = false, precision = 2, scale = 0)
	public float getPrecio() {
		return this.precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	@Column(name = "stock", nullable = false)
	public int getStock() {
		return this.stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	@Column(name = "estado", nullable = false)
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_creacion", nullable = false, length = 13)
	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Column(name = "imagen", nullable = false)
	public String getImagen() {
		return this.imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	@Column(name = "borrado", nullable = false)
	public boolean getBorrado() {
		return this.borrado;
	}

	public void setBorrado(boolean borrado) {
		this.borrado = borrado;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "cestas", joinColumns = {
			@JoinColumn(name = "productos_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "usuarios_id", nullable = false, updatable = false) })
	public Set<Usuarios> getUsuariosCesta() {
		return this.usuariosCesta;
	}

	public void setUsuariosCesta(Set<Usuarios> usuariosCesta) {
		this.usuariosCesta = usuariosCesta;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
	public Set<VentasProductos> getVentasProductos() {
		return this.ventasProductos;
	}

	public void setVentasProductos(Set<VentasProductos> ventasProductos) {
		this.ventasProductos = ventasProductos;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "favoritos", joinColumns = {
			@JoinColumn(name = "productos_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "usuarios_id", nullable = false, updatable = false) })
	public Set<Usuarios> getUsuariosFavorito() {
		return this.usuariosFavorito;
	}

	public void setUsuariosFavorito(Set<Usuarios> usuariosFavorito) {
		this.usuariosFavorito = usuariosFavorito;
	}

}
