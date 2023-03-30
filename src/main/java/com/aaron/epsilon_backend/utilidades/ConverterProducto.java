package com.aaron.epsilon_backend.utilidades;

import com.aaron.epsilon_backend.modelos.dto.ProductoDTO;
import com.aaron.epsilon_backend.modelos.entidades.Productos;

public class ConverterProducto {

	public static ProductoDTO convertirProducto(Productos producto) {
		ProductoDTO productoDTO = new ProductoDTO();
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
		return productoDTO;
	}
	
	public static Productos convertirProductoDTO(ProductoDTO productoDTO) {
		Productos producto = new Productos();
		producto.setId(productoDTO.getId());
		producto.setNombre(productoDTO.getNombre());
		producto.setDescripcion(productoDTO.getDescripcion());
		producto.setFechaCreacion(productoDTO.getFechaCreacion());
		producto.setStock(productoDTO.getStock());
		producto.setEstado(productoDTO.getEstado());
		producto.setUsuarios(ConverterUsuario.convertirUsuarioDTO(productoDTO.getUsuarios()));
		producto.setPrecio(productoDTO.getPrecio());
		producto.setDescuento(productoDTO.getDescuento());
		producto.setImagen(productoDTO.getImagen());
		producto.setBorrado(productoDTO.isBorrado());
		producto.setCategorias(ConverterCategoria.convertirCategoriaDTO(productoDTO.getCategorias()));
		return producto;
	}
}
