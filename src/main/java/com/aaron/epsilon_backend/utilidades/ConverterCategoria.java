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
	
	public static Categorias convertirCategoriaDTO(CategoriaDTO categoriaDTO) {
		Categorias categoria = new Categorias();
		categoria.setId(categoriaDTO.getId());
		categoria.setNombre(categoriaDTO.getNombre());
		return categoria;
	}
}
