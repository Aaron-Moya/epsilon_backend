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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aaron.epsilon_backend.modelos.dto.CestaDTO;
import com.aaron.epsilon_backend.modelos.entidades.Cestas;
import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.ICestasService;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IProductosService;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IUsuariosService;
import com.aaron.epsilon_backend.utilidades.Const;
import com.aaron.epsilon_backend.utilidades.ConverterCesta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/cesta")
public class CestasRestController {

	@Autowired
	private ICestasService cestasService;
	@Autowired
	private IUsuariosService usuariosService;
	@Autowired
	private IProductosService productosService;
	
	@GetMapping
    @Operation(
    		summary = "Devuelve todas las cestas", description = "Devuelve todas las cestas",
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
    	List<Cestas> listaCestas = new ArrayList<>();
    	List<CestaDTO> listaCestasDto = new ArrayList<>();
		Map<String,Object> response = new HashMap<>();
		
		try {
			listaCestas = cestasService.findAll();
			listaCestasDto = listaCestas.stream()
					.map(ConverterCesta::convertirCesta).toList();
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(listaCestasDto,HttpStatus.OK);
    }
	
	@GetMapping("/{id}")
    @Operation(
    		summary = "Devuelve una cesta dado un id", description = "Devuelve una cesta dado un id",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "OK",
    						content = @Content()),
    				@ApiResponse(
    						responseCode = "404",
    						description = "No se ha encontrado una cesta con ese id",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = Const.ERROR_BD,
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		Cestas cesta = null;
		CestaDTO cestaDto = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			cesta = cestasService.findById(id);
			if (cesta != null)
				cestaDto = ConverterCesta.convertirCesta(cesta);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(cesta==null) { // El id no existe
			response.put(Const.MENSAJE, "La cesta con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(cestaDto,HttpStatus.OK);
	}
	
	@PostMapping()
	@Operation(
    		summary = "Añade un producto a la cesta", description = "Si no hay productos crea una cesta nueva, si no simplemente añade 1 a la cantidad",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "¡Producto añadido a la cesta correctamente!",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "¡Error al añadir el producto a la cesta",
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> add(@RequestParam long idUsuario, @RequestParam long idProducto){
		Cestas nuevaCesta = new Cestas();
		Cestas cestaExiste = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			Usuarios usuario = usuariosService.findById(idUsuario);
			Productos producto = productosService.findById(idProducto);
			cestaExiste = cestasService.findByUsuarioAndProducto(usuario, producto);
			
			if (cestaExiste == null) {
				nuevaCesta.setUsuario(usuario);
				nuevaCesta.setProducto(producto);
				nuevaCesta.setCantidad(1);
				cestasService.save(nuevaCesta);
			}
			else {
				cestaExiste.setCantidad(cestaExiste.getCantidad() + 1);
				cestasService.save(cestaExiste);
			}
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		response.put(Const.MENSAJE, "Producto añadido a la cesta correctamente");
		/*if (cestaExiste == null)
			response.put(Const.MENSAJE, "Se ha creado una nueva cesta");
		else
			response.put(Const.MENSAJE, "Se ha actualizado una cesta ya existente");*/
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PutMapping("/disminuirCantidad")
	@Operation(
    		summary = "Baja la cantidad de un producto", description = "Baja la cantidad de un producto",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "¡Cantidad bajada correctamente!",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "¡Error al bajar la cantidad",
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> disminuirCantidad(@RequestParam long idUsuario, @RequestParam long idProducto){
		Cestas cestaExiste = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			Usuarios usuario = usuariosService.findById(idUsuario);
			Productos producto = productosService.findById(idProducto);
			cestaExiste = cestasService.findByUsuarioAndProducto(usuario, producto);
			
			if (cestaExiste.getCantidad() > 1) {
				cestaExiste.setCantidad(cestaExiste.getCantidad() - 1);
				cestasService.save(cestaExiste);
			}
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		response.put(Const.MENSAJE, "Cantidad bajada correctamente");
		/*if (cestaExiste == null)
			response.put(Const.MENSAJE, "Se ha creado una nueva cesta");
		else
			response.put(Const.MENSAJE, "Se ha actualizado una cesta ya existente");*/
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@DeleteMapping()
	@Operation(
    		summary = "Elimina un producto de la cesta", description = "Elimina un producto de la cesta",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "¡Producto eliminado de la cesta!",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "¡Error al elimiar el producto de la cesta!",
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> deleteProductoCesta(@RequestParam long idCesta) {
		Map<String,Object> response = new HashMap<>();
		
		try {
			cestasService.delete(idCesta);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(Const.MENSAJE, "Se ha eliminado el producto de la cesta correctamente!");
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
}
