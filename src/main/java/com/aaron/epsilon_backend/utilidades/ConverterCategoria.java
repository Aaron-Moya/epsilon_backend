package com.aaron.epsilon_backend.utilidades;

import com.aaron.epsilon_backend.modelos.dto.CategoriaDTO;
import com.aaron.epsilon_backend.modelos.entidades.Categorias;

public class ConverterCategoria {

	public static CategoriaDTO convertirCategoria(Categorias categoria) {
		CategoriaDTO categoriaDTO = new CategoriaDTO();
		categoriaDTO.setId(categoria.getId());
		categoriaDTO.setNombre(categoria.getNombre());
		return categoriaDTO;
	}
}
