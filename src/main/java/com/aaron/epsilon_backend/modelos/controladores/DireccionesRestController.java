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

import com.aaron.epsilon_backend.modelos.entidades.Direcciones;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IDireccionesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/direcciones")
public class DireccionesRestController {

	@Autowired
	private IDireccionesService direccionesService;
	
	@GetMapping
    @Operation(
    		summary = "Devuelve todas las direcciones", description = "Devuelve todas las direcciones",
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
    	List<Direcciones> listaDirecciones = new ArrayList<>();
		Map<String,Object> response = new HashMap<>();
		
		try {
			listaDirecciones = direccionesService.findAll();
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put("mensaje", "Error al conectar con la base de datos");
			response.put("error", e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<Direcciones>>(listaDirecciones,HttpStatus.OK);
    }
	
	@GetMapping("/{id}")
    @Operation(
    		summary = "Devuelve una dirección dado un id", description = "Devuelve una dirección dado un id",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "OK",
    						content = @Content()),
    				@ApiResponse(
    						responseCode = "404",
    						description = "No se ha encontrado una dirección con ese id",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "Error al conectar con la base de datos",
    						content = @Content())
    		})
	public ResponseEntity<?> getById(@PathVariable Long id) {
		Direcciones direccion = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			direccion = direccionesService.findById(id);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put("mensaje", "Error al conectar con la base de datos");
			response.put("error", e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(direccion==null) { // El id no existe
			response.put("mensaje", "La dirección con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Direcciones>(direccion,HttpStatus.OK);
	}
	
	/*@PostMapping()
	@Operation(
    		summary = "Crea un direccion", description = "Crea un direccion",
    		responses = {
    				@ApiResponse(
    						responseCode = "201",
    						description = "¡Dirección correctamente!",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "¡Error al crear la dirección!",
    						content = @Content())
    		})
	public ResponseEntity<?> create(@RequestBody Direcciones direccion, BindingResult result){
		Direcciones nuevaDireccion = null;
		Map<String,Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			nuevaDireccion = direccionesService.save(direccion);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put("mensaje", "Error al conectar con la base de datos");
			response.put("error", e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "La direccion se ha insertado correctamente");
		response.put("direccion", nuevaDireccion);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}*/
	
}
