package com.aaron.epsilon_backend.modelos.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.aaron.epsilon_backend.modelos.dto.ProductoDTO;
import com.aaron.epsilon_backend.modelos.dto.UsuarioCrearDTO;
import com.aaron.epsilon_backend.modelos.dto.UsuarioDTO;
import com.aaron.epsilon_backend.modelos.dto.UsuarioLoginDTO;
import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IProductosService;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IUsuariosService;
import com.aaron.epsilon_backend.upload.FicherosController;
import com.aaron.epsilon_backend.upload.IStorageService;
import com.aaron.epsilon_backend.utilidades.ConverterProducto;
import com.aaron.epsilon_backend.utilidades.ConverterUsuario;
import com.aaron.epsilon_backend.utilidades.Utilidades;
import com.aaron.epsilon_backend.utilidades.Const;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuariosRestController {

	@Autowired
	private final IUsuariosService usuariosService;
	@Autowired
	private final IProductosService productosService;
	@Autowired
	private final IStorageService storageService;
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(UsuariosRestController.class);
	
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
    	List<UsuarioDTO> listaUsuariosDtos = new ArrayList<>();
		Map<String,Object> response = new HashMap<>();
		
		try {
			listaUsuarios = usuariosService.findAll();
			listaUsuariosDtos = listaUsuarios.stream()
					.map(ConverterUsuario::convertirUsuario).toList();
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(listaUsuariosDtos,HttpStatus.OK);
    }
	
	@GetMapping("/id/{id}")
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
    						description = "Error en la base de datos",
    						content = @Content())
    		})
	public ResponseEntity<?> getById(@PathVariable Long id){
		Usuarios usuario = null;
		UsuarioLoginDTO usuarioDTO = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			usuario = usuariosService.findById(id);
			usuarioDTO = ConverterUsuario.convertirUsuarioLogin(usuario);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(usuario==null) { // El id no existe
			response.put(Const.MENSAJE, "El usuario con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(usuarioDTO,HttpStatus.OK);
	}
	
	@GetMapping("/correo/{correo}")
    @Operation(
    		summary = "Devuelve un usuario dado un email", description = "Devuelve un usuario dado un email",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "OK",
    						content = @Content()),
    				@ApiResponse(
    						responseCode = "404",
    						description = "No se ha encontrado un usuario con ese email",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "Error en la base de datos",
    						content = @Content())
    		})
	public ResponseEntity<?> getByEmail(@PathVariable String correo){
		Usuarios usuario = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			usuario = usuariosService.findByCorreo(correo);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(usuario==null) { // El email no existe
			response.put(Const.MENSAJE, "El usuario con email: ".concat(correo.concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(usuario,HttpStatus.OK);
	}
	
	@GetMapping("/login")
    @Operation(
    		summary = "Comprueba las credenciales de un usuario", description = "Comprueba las credenciales de un usuario",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "OK",
    						content = @Content()),
    				@ApiResponse(
    						responseCode = "404",
    						description = "No se ha encontrado un usuario con ese email y contraseña",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "Error en la base de datos",
    						content = @Content())
    		})
	public Usuarios comprobarCredenciales(@RequestParam String correo, @RequestParam String password){
		Usuarios usuario = null;		
		usuario = usuariosService.findByCorreo(correo);
		
		if (usuario != null && (!usuario.getPassword().equals(Utilidades.encriptarSHA256(password)))) { // Comprueba la contraseña
				usuario = null; 
		}
		return usuario;
	}
	
	@GetMapping("/favoritos")
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> getProductosFavoritos(@RequestParam long idUsuario) {
		Usuarios usuario = usuariosService.findById(idUsuario);
    	Set<Productos> listaProductos = null;
    	List<ProductoDTO> listaProductosDTO = new ArrayList<>();
		Map<String,Object> response = new HashMap<>();
		
		try {
			listaProductos = usuario.getProductosFavoritos();
			listaProductosDTO = listaProductos.stream()
					.map(ConverterProducto::convertirProducto).toList();
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(listaProductosDTO,HttpStatus.OK);
    }
	
	@PostMapping(value = "/registro")
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
			@RequestPart UsuarioCrearDTO usuario, @RequestPart(required = false) MultipartFile file,
			BindingResult result){
		Usuarios nuevoUsuario = new Usuarios();
		Map<String,Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.toList();
			
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		// Almacenamos el fichero y obtenemos su URL
		String urlImagen = null;
		if (Objects.nonNull(file) && !file.isEmpty()) {
			String imagen = storageService.store(file);
			urlImagen = MvcUriComponentsBuilder
							// El segundo argumento es necesario solo cuando queremos obtener la imagen
							// En este caso tan solo necesitamos obtener la URL
							.fromMethodName(FicherosController.class, "serveFile", imagen, null)  
							.build().toUriString();
		}
		else {
			urlImagen = "http://localhost:8080/files/default_usuario.png";
		}
		
		try {
			nuevoUsuario.setUsuario(usuario.getUsuario());
			nuevoUsuario.setPassword(Utilidades.encriptarSHA256(usuario.getPassword()));
			nuevoUsuario.setCorreo(usuario.getCorreo());
			nuevoUsuario.setAvatar(urlImagen);
			nuevoUsuario.setFechaCreacion(new Date());
			nuevoUsuario = usuariosService.save(nuevoUsuario);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(Const.MENSAJE, "El usuario se ha insertado correctamente");
		response.put("usuario", nuevoUsuario);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/favorito")
	@Operation(
    		summary = "Añade un producto a favoritos", description = "Añade un producto a favoritos",
    		responses = {
    				@ApiResponse(
    						responseCode = "201",
    						description = "¡Producto añadido a favoritos!",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "¡Error al añadir el producto a favoritos!",
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> addProductoFavorito(@RequestParam long idUsuario, 
			@RequestParam long idProducto){
		Usuarios usuarioActualizado = usuariosService.findById(idUsuario);
		Productos producto = productosService.findById(idProducto);
		Map<String,Object> response = new HashMap<>();
		
		try {
			usuarioActualizado.getProductosFavoritos().add(producto);
			usuariosService.save(usuarioActualizado);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put("mensaje", "Error al conectar con la base de datos");
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Se ha añadido correctamente le producto a favoritos");
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/favorito")
	@Operation(
    		summary = "Elimina un producto de favoritos", description = "Elimina un producto de favoritos",
    		responses = {
    				@ApiResponse(
    						responseCode = "201",
    						description = "¡Producto eliminado de favoritos!",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "¡Error al elimiar el producto de favoritos!",
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> deleteProductoFavorito(@RequestParam long idUsuario, 
			@RequestParam long idProducto){
		Usuarios usuarioActualizado = usuariosService.findById(idUsuario);
		Productos producto = productosService.findById(idProducto);
		Map<String,Object> response = new HashMap<>();
		
		try {
			usuarioActualizado.getProductosFavoritos().remove(producto);
			usuariosService.save(usuarioActualizado);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(Const.MENSAJE, "Se ha eliminado el producto de favoritos correctamente!");
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
