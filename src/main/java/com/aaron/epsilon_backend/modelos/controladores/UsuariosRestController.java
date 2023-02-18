package com.aaron.epsilon_backend.modelos.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IUsuariosService;
import com.aaron.epsilon_backend.upload.FicherosController;
import com.aaron.epsilon_backend.upload.IStorageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuariosRestController {

	@Autowired
	private final IUsuariosService usuariosService;
	@Autowired
	private final IStorageService storageService;
	
	@GetMapping
    @Operation(
    		summary = "Devuelve todos los usuarios", description = "Devuelve todos los usuarios",
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
    	List<Usuarios> listaUsuarios = new ArrayList<>();
		Map<String,Object> response = new HashMap<>();
		
		try {
			listaUsuarios = usuariosService.findAll();
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put("mensaje", "Error al conectar con la base de datos");
			response.put("error", e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<Usuarios>>(listaUsuarios,HttpStatus.OK);
    }
	
	@GetMapping("/{id}")
    @Operation(
    		summary = "Devuelve un usuario dado un id", description = "Devuelve un usuario dado un id",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "OK",
    						content = @Content()),
    				@ApiResponse(
    						responseCode = "404",
    						description = "No se ha encontrado un usuario con ese id",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "Error al conectar con la base de datos",
    						content = @Content())
    		})
	public ResponseEntity<?> getById(@PathVariable Long id){
		Usuarios usuario = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			usuario = usuariosService.findById(id);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put("mensaje", "Error al conectar con la base de datos");
			response.put("error", e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(usuario==null) { // El id no existe
			response.put("mensaje", "La categoría con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Usuarios>(usuario,HttpStatus.OK);
	}
	
	@PostMapping(value = "", consumes= MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
    		summary = "Crea un usuario", description = "Crea un usuario",
    		responses = {
    				@ApiResponse(
    						responseCode = "201",
    						description = "¡Usuario creado correctamente!",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "¡Error al crear el usuario!",
    						content = @Content())
    		})
	public ResponseEntity<?> create(
			@RequestPart("usuario") @Parameter(schema = @Schema(type = "string", format = "binary")) Usuarios usuario,
			@RequestPart("file") MultipartFile file, 
			BindingResult result){
		Usuarios nuevoUsuario = null;
		Map<String,Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		// Almacenamos el fichero y obtenemos su URL
		String urlImagen = null;
		if (!file.isEmpty()) {
			String imagen = storageService.store(file);
			urlImagen = MvcUriComponentsBuilder
							// El segundo argumento es necesario solo cuando queremos obtener la imagen
							// En este caso tan solo necesitamos obtener la URL
							.fromMethodName(FicherosController.class, "serveFile", imagen, null)  
							.build().toUriString();
		}
		
		try {
			usuario.setAvatar(urlImagen);
			nuevoUsuario = usuariosService.save(usuario);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put("mensaje", "Error al conectar con la base de datos");
			response.put("error", e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El usuario se ha insertado correctamente");
		response.put("usuario", nuevoUsuario);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
}
