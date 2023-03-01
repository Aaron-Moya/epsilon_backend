package com.aaron.epsilon_backend.utilidades;

import com.aaron.epsilon_backend.modelos.dto.ProductoDTO;
import com.aaron.epsilon_backend.modelos.entidades.Productos;

public class ConverterProducto {

	public static ProductoDTO convertirProducto(Productos producto) {
		ProductoDTO productoDTO = new ProductoDTO();
		productoDTO.setId(producto.getId());
		productoDTO.setNombre(producto.getNombre());
		productoDTO.setEstado(producto.getEstado());
		productoDTO.setPrecio(producto.getPrecio());
		productoDTO.setImagen(producto.getImagen());
		return productoDTO;
	}
}
