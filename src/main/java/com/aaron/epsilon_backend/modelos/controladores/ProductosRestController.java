package com.aaron.epsilon_backend.modelos.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IProductosService;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IUsuariosService;
import com.aaron.epsilon_backend.upload.FicherosController;
import com.aaron.epsilon_backend.upload.IStorageService;
import com.aaron.epsilon_backend.utilidades.Const;
import com.aaron.epsilon_backend.utilidades.ConverterProducto;
import com.aaron.epsilon_backend.utilidades.FiltroCategoria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/productos")
public class ProductosRestController {

	@Autowired
	private IProductosService productosService;
	@Autowired
	private IUsuariosService usuariosService;
	@Autowired
	private IStorageService storageService;
		
	@GetMapping
    @Operation(
    		
    		summary = "Devuelve todos los productos", description = "Devuelve todos los productos",
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
	@PageableAsQueryParam
    public ResponseEntity<?> getAll(Pageable page) {
    	Page<Productos> listaProductos = null;
    	List<ProductoDTO> listaProductosDTO = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			listaProductos = productosService.findAll(page);
			listaProductosDTO = listaProductos.getContent().stream()
					.map(ConverterProducto::convertirProducto).toList();
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new PageImpl<>(listaProductosDTO, page, listaProductos.getTotalElements()) ,HttpStatus.OK);
    }
	
	@GetMapping("/id/{id}")
    @Operation(
    		summary = "Devuelve un producto dado un id", description = "Devuelve un producto dado un id",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "OK",
    						content = @Content()),
    				@ApiResponse(
    						responseCode = "404",
    						description = "No se ha encontrado un producto con ese id",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "Error en la base de datos",
    						content = @Content())
    		})
	public ResponseEntity<?> getById(@PathVariable Long id){
		Productos producto = null;
		ProductoDTO productoDto = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			producto = productosService.findById(id);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, "Error al conectar con la base de datos");
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(producto==null) { // El id no existe
			response.put(Const.MENSAJE, "El producto con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		productoDto = ConverterProducto.convertirProducto(producto);
		return new ResponseEntity<>(productoDto,HttpStatus.OK);
	}
	
	@GetMapping("/filtro")
	@Operation(
    		summary = "Devuelve productos dado unos filtros", description = "Devuelve productos dado unos filtros\"",
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
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> getProductosByFiltro(Pageable page, FiltroCategoria filtro) {
		Page<Productos> listaProductos = null;
    	List<ProductoDTO> listaProductosDTO = new ArrayList<>();
		Map<String,Object> response = new HashMap<>();
		
		try {
			listaProductos= productosService.findByFiltroCategoria(filtro, page);
			listaProductosDTO = listaProductos.stream()
					.map(ConverterProducto::convertirProducto).toList();
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new PageImpl<>(listaProductosDTO, page, listaProductos.getTotalElements()) ,HttpStatus.OK);
    }
	
	@GetMapping("/favoritos")
	@Operation(
    		summary = "Devuelve todos los productos favoritos de un usuario", description = "Devuelve todos los productos favoritos de un usuario",
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
	
	@GetMapping("/usuario")
    @Operation(
    		summary = "Devuelve todos los productos de un usuario", description = "Devuelve todos los productos de un usuario",
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
	@PageableAsQueryParam
	@SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<?> getByUsuario(Pageable page, @RequestParam long idUsuario) {
		Usuarios usuario = usuariosService.findById(idUsuario);
    	Page<Productos> listaProductos = null;
    	List<ProductoDTO> listaProductosDTO = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			listaProductos = productosService.findByUsuarios(page, usuario);
			listaProductosDTO = listaProductos.getContent().stream()
					.map(ConverterProducto::convertirProducto).toList();
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new PageImpl<>(listaProductosDTO, page, listaProductos.getTotalElements()) ,HttpStatus.OK);
    }
	
	@PostMapping(value = "")
	@Operation(
    		summary = "Crea un producto", description = "Crea un producto",
    		responses = {
    				@ApiResponse(
    						responseCode = "201",
    						description = "¡Producto creado correctamente!",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "¡Error al crear el producto!",
    						content = @Content())
    		})
	public ResponseEntity<?> create(
			@RequestPart Productos producto, 
			@RequestPart MultipartFile file,
			BindingResult result){
		Productos nuevoProducto;
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
		String imagen = storageService.store(file);
		urlImagen = MvcUriComponentsBuilder
						// El segundo argumento es necesario solo cuando queremos obtener la imagen
						// En este caso tan solo necesitamos obtener la URL
						.fromMethodName(FicherosController.class, "serveFile", imagen, null)  
						.build().toUriString();
		
		try {
			producto.setImagen(urlImagen);
			producto.setFechaCreacion(new Date());
			nuevoProducto = productosService.save(producto);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(Const.MENSAJE, "El producto se ha insertado correctamente");
		response.put("producto", nuevoProducto);
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
	
	@PutMapping(value = "")
	@Operation(
    		summary = "Modifica un producto", description = "Modifica un producto",
    		responses = {
    				@ApiResponse(
    						responseCode = "201",
    						description = "¡Producto modificado correctamente!",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "¡Error al modificar el producto!",
    						content = @Content())
    		})
	public ResponseEntity<?> update(@RequestPart ProductoDTO producto, @RequestPart(required = false) MultipartFile file, BindingResult result){
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
		if (file != null) {
			String urlImagen = null;
			String imagen = storageService.store(file);
			urlImagen = MvcUriComponentsBuilder
							.fromMethodName(FicherosController.class, "serveFile", imagen, null)  
							.build().toUriString();
			producto.setImagen(urlImagen);
		}
		
		try {
			productosService.save(ConverterProducto.convertirProductoDTO(producto));
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(Const.MENSAJE, "El producto se ha modificado correctamente");
		return new ResponseEntity<>(response,HttpStatus.OK);
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
    						responseCode = "400",
    						description = "¡No puedes añadir tu propio producto a favoritos!",
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
		
		if (idUsuario == producto.getUsuarios().getId()) {
			response.put(Const.MENSAJE, "¡No puedes añadir tu propio producto a favoritos!");
			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		}
		
		try {
			usuarioActualizado.getProductosFavoritos().add(producto);
			usuariosService.save(usuarioActualizado);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, "Error al conectar con la base de datos");
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Se ha añadido correctamente le producto a favoritos");
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@DeleteMapping()
	@Operation(
    		summary = "Elimina un producto", description = "Elimina un producto",
    		responses = {
    				@ApiResponse(
    						responseCode = "201",
    						description = "¡Producto eliminado!",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "¡Error al elimiar el producto!",
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> delete(@RequestParam long idProducto) {
		Productos producto = productosService.findById(idProducto);
		Map<String,Object> response = new HashMap<>();
		
		try {
			producto.setBorrado(true);
			productosService.save(producto);
			// Borra el producto de los favoritos de los usuarios
			usuariosService.findAll().forEach(usuario -> {
				if (usuario.getProductosFavoritos().contains(producto))
					usuario.getProductosFavoritos().remove(producto);
				usuariosService.save(usuario);
			});
			
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(Const.MENSAJE, "Se ha eliminado el producto correctamente!");
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
			@RequestParam long idProducto) {
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
