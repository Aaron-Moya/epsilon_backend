package com.aaron.epsilon_backend.modelos.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * VentasProductos generated by hbm2java
 */
@Entity
@Table(name = "ventas_productos")
public class VentasProductos implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private Productos producto;
	private Ventas venta;
	private int cantidad;

	public VentasProductos() {
	}

	public VentasProductos(int id, Productos producto, Ventas venta, int cantidad) {
		this.id = id;
		this.producto = producto;
		this.venta = venta;
		this.cantidad = cantidad;
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
	@JoinColumn(name = "id_producto", nullable = false)
	public Productos getProducto() {
		return this.producto;
	}

	public void setProducto(Productos producto) {
		this.producto = producto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_venta", nullable = false)
	public Ventas getVenta() {
		return this.venta;
	}

	public void setVenta(Ventas venta) {
		this.venta = venta;
	}

	@Column(name = "cantidad", nullable = false)
	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
