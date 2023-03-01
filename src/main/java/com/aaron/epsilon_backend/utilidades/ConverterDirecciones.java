package com.aaron.epsilon_backend.utilidades;

import com.aaron.epsilon_backend.modelos.dto.DireccionDto;
import com.aaron.epsilon_backend.modelos.entidades.Direcciones;

public class ConverterDirecciones {

	public static DireccionDto convertirDireccion(Direcciones direccion) {
		DireccionDto direccionDto = new DireccionDto();
		direccionDto.setId(direccion.getId());
		direccionDto.setCiudad(direccion.getCiudad());
		direccionDto.setCalle(direccion.getCalle());
		direccionDto.setNumero(direccion.getNumero());
		return direccionDto;
	}
}
