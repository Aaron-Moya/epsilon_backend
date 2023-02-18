package com.aaron.epsilon_backend.modelos.entidades;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

@Entity
@Table(name = "usuarios")
public class Usuarios implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private Direcciones direcciones;
	private String usuario;
	private String password;
	private String correo;
	private Date fechaCreacion;
	private String avatar;
	private Set<Valoraciones> valoracionesRecibidas = new HashSet<Valoraciones>(0);
	private Set<Ventas> ventasRealizadas = new HashSet<Ventas>(0);
	private Set<Productos> productosCesta = new HashSet<Productos>(0);
	private Set<Valoraciones> valoracionesDadas = new HashSet<Valoraciones>(0);
	private Set<Productos> productosFavoritos = new HashSet<Productos>(0);
	private Set<Ventas>  comprasRealizadas = new HashSet<Ventas>(0);

	public Usuarios() {
	}

	public Usuarios(int id, String usuario, String password, String correo, Date fechaCreacion, String avatar) {
		this.id = id;
		this.usuario = usuario;
		this.password = password;
		this.correo = correo;
		this.fechaCreacion = fechaCreacion;
		this.avatar = avatar;
	}

	public Usuarios(int id, Direcciones direcciones, String usuario, String password, String correo, Date fechaCreacion,
			String avatar, Set<Valoraciones> valoracionesRecibidas, Set<Ventas> ventasRealizadas, Set<Productos> productosCesta,
			Set<Valoraciones> valoracionesDadas, Set<Productos> productosFavoritos, Set<Ventas> comprasRealizadas) {
		this.id = id;
		this.direcciones = direcciones;
		this.usuario = usuario;
		this.password = password;
		this.correo = correo;
		this.fechaCreacion = fechaCreacion;
		this.avatar = avatar;
		this.valoracionesRecibidas = valoracionesRecibidas;
		this.ventasRealizadas = ventasRealizadas;
		this.productosCesta = productosCesta;
		this.valoracionesDadas = valoracionesDadas;
		this.productosFavoritos = productosFavoritos;
		this.comprasRealizadas = comprasRealizadas;
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
	@JoinColumn(name = "id_direccion")
	public Direcciones getDirecciones() {
		return this.direcciones;
	}

	public void setDirecciones(Direcciones direcciones) {
		this.direcciones = direcciones;
	}

	@Column(name = "usuario", nullable = false, length = 18)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "correo", nullable = false, length = 30)
	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_creacion", nullable = false, length = 13)
	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Column(name = "avatar", nullable = false)
	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioValorado")
	public Set<Valoraciones> getValoracionesRecibidas() {
		return this.valoracionesRecibidas;
	}

	public void setValoracionesRecibidas(Set<Valoraciones> valoracionesRecibidas) {
		this.valoracionesRecibidas = valoracionesRecibidas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioVendedor")
	public Set<Ventas> getVentasRealizadas() {
		return this.ventasRealizadas;
	}

	public void setVentasRealizadas(Set<Ventas> ventasRealizadas) {
		this.ventasRealizadas = ventasRealizadas;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "cestas", joinColumns = {
			@JoinColumn(name = "usuarios_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "productos_id", nullable = false, updatable = false) })
	public Set<Productos> getProductosCesta() {
		return this.productosCesta;
	}

	public void setProductosCesta(Set<Productos> productos) {
		this.productosCesta = productos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioValorador")
	public Set<Valoraciones> getValoracionesDadas() {
		return this.valoracionesDadas;
	}

	public void setValoracionesDadas(Set<Valoraciones> valoracionesDadas) {
		this.valoracionesDadas = valoracionesDadas;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "favoritos", joinColumns = {
			@JoinColumn(name = "usuarios_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "productos_id", nullable = false, updatable = false) })
	public Set<Productos> getProductosFavoritos() {
		return this.productosFavoritos;
	}

	public void setProductosFavoritos(Set<Productos> productos) {
		this.productosFavoritos = productos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarioComprador")
	public Set<Ventas> getComprasRealizadas() {
		return this.comprasRealizadas;
	}

	public void setComprasRealizadas(Set<Ventas> ventas) {
		this.comprasRealizadas = ventas;
	}

}
