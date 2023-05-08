package com.aaron.epsilon_backend.utilidades;

import com.aaron.epsilon_backend.modelos.dto.VentasProductosDTO;
import com.aaron.epsilon_backend.modelos.entidades.VentasProductos;

public class ConverterVentaProducto {

	public static VentasProductosDTO convertirVentaProducto(VentasProductos ventaProducto) {
		VentasProductosDTO ventaProductoDTO = new VentasProductosDTO();
		ventaProductoDTO.setId(ventaProducto.getId());
		ventaProductoDTO.setIdVenta(ventaProducto.getVenta().getId());
		ventaProductoDTO.setFechaVenta(ventaProducto.getVenta().getFechaVenta());
		ventaProductoDTO.setProducto(ConverterProducto.convertirProducto(ventaProducto.getProducto()));
		ventaProductoDTO.setCantidad(ventaProducto.getCantidad());
		ventaProductoDTO.setTotal((ventaProducto.getProducto().getPrecio() - ventaProducto.getProducto().getDescuento()) * ventaProducto.getCantidad());
		return ventaProductoDTO;
	}

}
