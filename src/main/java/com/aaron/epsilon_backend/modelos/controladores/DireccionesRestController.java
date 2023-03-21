package com.aaron.epsilon_backend.modelos.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aaron.epsilon_backend.modelos.dto.DireccionDto;
import com.aaron.epsilon_backend.modelos.entidades.Direcciones;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IDireccionesService;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IUsuariosService;
import com.aaron.epsilon_backend.utilidades.Const;
import com.aaron.epsilon_backend.utilidades.ConverterDirecciones;
import com.aaron.epsilon_backend.utilidades.ConverterUsuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/direcciones")
public class DireccionesRestController {

	@Autowired
	private IDireccionesService direccionesService;
	@Autowired
	private IUsuariosService usuariosService;
	
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
    						description = Const.ERROR_BD,
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getAll() {
    	List<Direcciones> listaDirecciones = new ArrayList<>();
    	List<DireccionDto> listaDireccionesDto = new ArrayList<>();
		Map<String,Object> response = new HashMap<>();
		
		try {
			listaDirecciones = direccionesService.findAll();
			listaDireccionesDto = listaDirecciones.stream()
					.map(ConverterDirecciones::convertirDireccion).toList();
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(listaDireccionesDto,HttpStatus.OK);
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
    						description = Const.ERROR_BD,
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		Direcciones direccion = null;
		DireccionDto direccionDto = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			direccion = direccionesService.findById(id);
			if (direccion != null)
				direccionDto = ConverterDirecciones.convertirDireccion(direccion);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put("error", e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(direccion==null) { // El id no existe
			response.put(Const.MENSAJE, "La dirección con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(direccionDto,HttpStatus.OK);
	}
	
	@PostMapping()
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
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> create(@RequestBody Direcciones direccion, @RequestParam long idUsuario, 
			BindingResult result){
		Direcciones nuevaDireccion = null;
		Set<Usuarios> usuarios = new HashSet<>();
		Map<String,Object> response = new HashMap<>();
		
		Usuarios usuario = usuariosService.findById(idUsuario);
		usuarios.add(usuariosService.findById(idUsuario));
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.toList();
			
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			direccion.setUsuarios(usuarios);
			nuevaDireccion = direccionesService.save(direccion);
			usuario.setDireccion(nuevaDireccion);
			usuariosService.save(usuario);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put("error", e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(Const.MENSAJE, "La direccion se ha insertado correctamente");
		response.put("direccion", nuevaDireccion);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	@PutMapping()
	@Operation(
    		summary = "Actualiza una dirección", description = "Actualiza una dirección",
    		responses = {
    				@ApiResponse(
    						responseCode = "201",
    						description = "¡Dirección actualizada correctamente!",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "¡Error al actualizar la dirección!",
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> update(@RequestBody Direcciones direccion, 
			BindingResult result){
		Direcciones direccionActualizada = null;
		//Set<Usuarios> usuarios = new HashSet<>();
		Map<String,Object> response = new HashMap<>();
		
		//Usuarios usuario = usuariosService.findById(idUsuario);
		//usuarios.add(usuariosService.findById(idUsuario));
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.toList();
			
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			//direccion.setUsuarios(usuarios);
			direccionActualizada = direccionesService.save(direccion);
			//usuario.setDireccion(nuevaDireccion);
			//usuariosService.save(usuario);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put("error", e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(Const.MENSAJE, "La direccion se ha actualizado correctamente");
		response.put("direccion", direccionActualizada);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
}
