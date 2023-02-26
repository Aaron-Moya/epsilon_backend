package com.aaron.epsilon_backend.modelos.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.aaron.epsilon_backend.modelos.dto.ProductoDTO;
import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IProductosService;
import com.aaron.epsilon_backend.upload.FicherosController;
import com.aaron.epsilon_backend.upload.IStorageService;
import com.aaron.epsilon_backend.utilidades.ConverterProducto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/productos")
public class ProductosRestController {

	@Autowired
	private IProductosService productosService;
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
			response.put("mensaje", "Error al conectar con la base de datos");
			response.put("error", e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Page<ProductoDTO>>(new PageImpl<>(listaProductosDTO, page, listaProductos.getTotalElements()) ,HttpStatus.OK);
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
		Map<String,Object> response = new HashMap<>();
		
		try {
			producto = productosService.findById(id);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put("mensaje", "Error al conectar con la base de datos");
			response.put("error", e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(producto==null) { // El id no existe
			response.put("mensaje", "El producto con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Productos>(producto,HttpStatus.OK);
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
		Productos nuevoProducto = new Productos();
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
		String imagen = storageService.store(file);
		urlImagen = MvcUriComponentsBuilder
						// El segundo argumento es necesario solo cuando queremos obtener la imagen
						// En este caso tan solo necesitamos obtener la URL
						.fromMethodName(FicherosController.class, "serveFile", imagen, null)  
						.build().toUriString();
		
		try {
			nuevoProducto = producto;
			nuevoProducto.setImagen(urlImagen);
			nuevoProducto.setFechaCreacion(new Date());
			nuevoProducto = productosService.save(nuevoProducto);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put("mensaje", "Error al conectar con la base de datos");
			response.put("error", e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El producto se ha insertado correctamente");
		response.put("producto", nuevoProducto);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
}
