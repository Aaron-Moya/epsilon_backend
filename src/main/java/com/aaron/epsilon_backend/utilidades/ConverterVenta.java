package com.aaron.epsilon_backend.utilidades;

import com.aaron.epsilon_backend.modelos.dto.VentasDTO;
import com.aaron.epsilon_backend.modelos.entidades.Ventas;

public class ConverterVenta {

	public static VentasDTO convertirVenta(Ventas venta) {
		VentasDTO ventaDTO = new VentasDTO();
		ventaDTO.setId(venta.getId());
		ventaDTO.setUsuarioComprador((long) venta.getUsuarioComprador().getId());
		ventaDTO.setFechaVenta(venta.getFechaVenta());
		ventaDTO.setTotal(venta.getTotal());
		return ventaDTO;
	}

}
