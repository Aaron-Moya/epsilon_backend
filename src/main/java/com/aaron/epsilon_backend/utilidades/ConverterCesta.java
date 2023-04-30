package com.aaron.epsilon_backend.utilidades;

import com.aaron.epsilon_backend.modelos.dto.CestaDTO;
import com.aaron.epsilon_backend.modelos.entidades.Cestas;

public class ConverterCesta {

	public static CestaDTO convertirCesta(Cestas cesta) {
		CestaDTO cestaDTO = new CestaDTO();
		cestaDTO.setId(cesta.getId());
		cestaDTO.setIdProducto(cesta.getProducto().getId());
		cestaDTO.setIdUsuario(cesta.getUsuario().getId());
		cestaDTO.setCantidad(cesta.getCantidad());
		return cestaDTO;
	}
	
	/*public static Cestas convertirCestaDTO(CestaDTO cestaDTO) {
		Direcciones direccion = new Direcciones();
		direccion.setId(cestaDTO.getId());
		direccion.setCiudad(cestaDTO.getCiudad());
		direccion.setCalle(cestaDTO.getCalle());
		direccion.setNumero(cestaDTO.getNumero());
		return direccion;
	}*/
}
