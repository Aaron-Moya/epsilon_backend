package com.aaron.epsilon_backend.utilidades;

import java.util.Date;

import com.aaron.epsilon_backend.modelos.dto.ProductoBibliotecaDTO;
import com.aaron.epsilon_backend.modelos.entidades.Productos;

public class ConverterProductoBiblioteca {

	public static ProductoBibliotecaDTO convertirProducto(Productos producto, int cantidad) {
		ProductoBibliotecaDTO productoDTO = new ProductoBibliotecaDTO();
		productoDTO.setId(producto.getId());
		productoDTO.setNombre(producto.getNombre());
		productoDTO.setDescripcion(producto.getDescripcion());
		productoDTO.setFechaCreacion(producto.getFechaCreacion());
		productoDTO.setStock(producto.getStock());
		productoDTO.setEstado(producto.getEstado());
		productoDTO.setUsuarios(ConverterUsuario.convertirUsuario(producto.getUsuarios()));
		productoDTO.setPrecio(producto.getPrecio());
		productoDTO.setDescuento(producto.getDescuento());
		productoDTO.setImagen(producto.getImagen());
		productoDTO.setBorrado(producto.getBorrado());
		productoDTO.setCategorias(ConverterCategoria.convertirCategoria(producto.getCategorias()));
		productoDTO.setCantidad(cantidad);
		return productoDTO;
	}
}
