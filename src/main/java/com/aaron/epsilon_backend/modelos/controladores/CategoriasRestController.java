package com.aaron.epsilon_backend.modelos.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aaron.epsilon_backend.modelos.dto.CategoriaDTO;
import com.aaron.epsilon_backend.modelos.entidades.Categorias;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.ICategoriasService;
import com.aaron.epsilon_backend.utilidades.Const;
import com.aaron.epsilon_backend.utilidades.ConverterCategoria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/categorias")
public class CategoriasRestController {

	@Autowired
	private ICategoriasService categoriasService;
	
    @GetMapping
    @Operation(
    		summary = "Devuelve todas las categorias", description = "Devuelve todas las categorias",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "OK",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "Error al conectar con la base de datos",
    						content = @Content())
    		})
    public ResponseEntity<?> getAll() {
    	List<Categorias> listaCategorias = new ArrayList<>();
    	List<CategoriaDTO> listaCategoriaDtos = new ArrayList<>();
		Map<String,Object> response = new HashMap<>();
		
		try {
			listaCategorias = categoriasService.findAll();
			listaCategoriaDtos = listaCategorias.stream()
					.map(ConverterCategoria::convertirCategoria).toList();
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(listaCategoriaDtos,HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @Operation(
    		summary = "Devuelve una categoría dado un id", description = "Devuelve una categoría dado un id",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "OK",
    						content = @Content()),
    				@ApiResponse(
    						responseCode = "404",
    						description = "No se ha encontrado una categoría con ese id",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "Error al conectar con la base de datos",
    						content = @Content())
    		})
	public ResponseEntity<?> getById(@PathVariable Long id){
		Categorias categoria = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			categoria = categoriasService.findById(id);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(categoria==null) { // El id no existe
			response.put("mensaje", "La categoría con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(categoria,HttpStatus.OK);
	}
}
