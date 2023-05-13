package com.aaron.epsilon_backend.modelos.controladores;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aaron.epsilon_backend.modelos.dto.ProductoCantidadDTO;
import com.aaron.epsilon_backend.modelos.dto.VentasDTO;
import com.aaron.epsilon_backend.modelos.dto.VentasProductosDTO;
import com.aaron.epsilon_backend.modelos.entidades.Cestas;
import com.aaron.epsilon_backend.modelos.entidades.Productos;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.modelos.entidades.Ventas;
import com.aaron.epsilon_backend.modelos.entidades.VentasProductos;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.ICestasService;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IProductosService;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IUsuariosService;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IVentasProductosService;
import com.aaron.epsilon_backend.modelos.servicios.interfaces.IVentasService;
import com.aaron.epsilon_backend.utilidades.Const;
import com.aaron.epsilon_backend.utilidades.ConverterVenta;
import com.aaron.epsilon_backend.utilidades.ConverterVentaProducto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/ventas")
public class VentasRestController {

	@Autowired
	private IVentasService ventasService;
	@Autowired
	private IVentasProductosService ventasProductosService;
	@Autowired
	private IUsuariosService usuariosService;
	@Autowired
	private IProductosService productosService;
	@Autowired
	private ICestasService cestasService;
	
	@GetMapping
    @Operation(
    		summary = "Devuelve todas las ventas", description = "Devuelve todas las ventas",
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
    	List<Ventas> listaVentas = new ArrayList<>();
		Map<String,Object> response = new HashMap<>();
		
		try {
			listaVentas = ventasService.findAll();
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(listaVentas,HttpStatus.OK);
    }
	
	@GetMapping("/{id}")
    @Operation(
    		summary = "Devuelve una venta dado un id", description = "Devuelve una venta dado un id",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "OK",
    						content = @Content()),
    				@ApiResponse(
    						responseCode = "404",
    						description = "No se ha encontrado una venta con ese id",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = Const.ERROR_BD,
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		Ventas venta = null;
		Map<String,Object> response = new HashMap<>();
		
		try {
			venta = ventasService.findById(id);
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(venta==null) { // El id no existe
			response.put(Const.MENSAJE, "La venta con ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(venta,HttpStatus.OK);
	}
	
	@GetMapping("/comprador/{idUsuario}")
    @Operation(
    		summary = "Devuelve las ventas de un usuario comprador", description = "Devuelve las ventas de un usuario comprador",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "OK",
    						content = @Content()),
    				@ApiResponse(
    						responseCode = "404",
    						description = "No se han encontrado ventas con ese id de usuario",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = Const.ERROR_BD,
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> getByUsuarioComprador(@PathVariable Long idUsuario) {
		List<Ventas> ventas = new ArrayList<>();
		List<VentasDTO> ventasDTOs = new ArrayList<>();
		Usuarios usuario = usuariosService.findById(idUsuario);
		Map<String,Object> response = new HashMap<>();
		
		if(usuario==null) { // El id no existe
			response.put(Const.MENSAJE, "El usuario con ID: ".concat(idUsuario.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			ventas = ventasService.findByUsuarioComprador(usuario);
			ventasDTOs = ventas.stream()
					.map(ConverterVenta::convertirVenta).toList();
			ventasDTOs.forEach(venta -> {
				ventasProductosService.findByVenta(ventasService.findById((long) venta.getId()))
					.forEach(ventaProducto -> {
						venta.setTotalProductos(venta.getTotalProductos() + ventaProducto.getCantidad());
					});//.size(); 
			});
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(ventasDTOs,HttpStatus.OK);
	}
	
	@GetMapping("/vendedor/{idUsuario}")
    @Operation(
    		summary = "Devuelve las ventas de un usuario vendedor", description = "Devuelve las ventas de un usuario vendedor",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "OK",
    						content = @Content()),
    				@ApiResponse(
    						responseCode = "404",
    						description = "No se han encontrado ventas con ese id de usuario",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = Const.ERROR_BD,
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> getByUsuarioVendedor(@PathVariable Long idUsuario) {
		List<Ventas> ventas = new ArrayList<>();
		List<VentasProductosDTO> ventasProductosDTOs = new ArrayList<>();
		List<VentasProductos> ventasProductos = new ArrayList<>();
		Usuarios usuario = usuariosService.findById(idUsuario);
		Map<String,Object> response = new HashMap<>();
		
		if(usuario==null) { // El id no existe
			response.put(Const.MENSAJE, "El usuario con ID: ".concat(idUsuario.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			//ventas = ventasService.findByUsuarioVendedor(usuario);
			ventasProductos = ventasProductosService.findAll();
			ventasProductos.forEach(ventaProducto -> {
				if (ventaProducto.getProducto().getUsuarios() == usuario) {
					ventasProductosDTOs.add(ConverterVentaProducto.convertirVentaProducto(ventaProducto));
				}
			});
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(ventasProductosDTOs,HttpStatus.OK);
	}
	
	@PostMapping()
	@Operation(
    		summary = "Crea una venta", description = "Crea una venta",
    		responses = {
    				@ApiResponse(
    						responseCode = "201",
    						description = "¡Venta creada correctamente!",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "¡Error al crear la venta!",
    						content = @Content())
    		})
	@SecurityRequirement(name = "Bearer Authentication")
	public ResponseEntity<?> create(@RequestBody List<ProductoCantidadDTO> productosCantidadDTOs,
			@RequestParam long idUsuarioComprador,
			//@RequestParam long idUsuarioVendedor,
			@RequestParam Float total,
			BindingResult result) {
		Map<String,Object> response = new HashMap<>();
		Ventas nuevaVenta = new Ventas();
		Set<VentasProductos> ventasProductos = new HashSet<>();
		List<Productos> productosVenta = new ArrayList<>();
		Usuarios usuarioComprador = usuariosService.findById(idUsuarioComprador);
		//Usuarios usuarioVendedor = usuariosService.findById(idUsuarioVendedor);
		productosCantidadDTOs.forEach(prod -> productosVenta.add(productosService.findById(prod.getId())));
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.toList();
			
			response.put("errors", errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			nuevaVenta.setFechaVenta(new Date());
			nuevaVenta.setUsuarioComprador(usuarioComprador);
			//nuevaVenta.setUsuarioVendedor(usuarioVendedor);
			nuevaVenta.setTotal(total);
			Ventas resultadoNuevaVenta = ventasService.save(nuevaVenta);
			productosVenta.forEach(producto -> {
				productosCantidadDTOs.forEach(prodCantDTO -> {
					if (prodCantDTO.getId() == producto.getId()) {
						VentasProductos ventaProducto = new VentasProductos();
						ventaProducto.setProducto(producto);
						ventaProducto.setVenta(resultadoNuevaVenta);
						ventaProducto.setCantidad(prodCantDTO.getCantidad());
						ventasProductos.add(ventaProducto);
					}
				});
			});
			
			ventasProductos.forEach(ventaProducto -> ventasProductosService.save(ventaProducto));
			resultadoNuevaVenta.setVentasProductos(ventasProductos);
			
			if (ventasService.save(resultadoNuevaVenta) != null) {
				List<Cestas> cestaUsuario = cestasService.findByUsuario(usuarioComprador);
				cestaUsuario.forEach(cesta -> {
					Productos producto = productosService.findById((long) cesta.getProducto().getId());
					producto.setStock(producto.getStock() - cesta.getCantidad());
					productosService.save(producto);
					cestasService.delete((long) cesta.getId());
				});
			}
		} catch (DataAccessException e) {  // Error al acceder a la base de datos
			response.put(Const.MENSAJE, Const.ERROR_BD);
			response.put(Const.ERROR, e.getMessage().concat(":")
					.concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put(Const.MENSAJE, "La venta se ha creado correctamente");
		return new ResponseEntity<>(response,HttpStatus.CREATED);
	}
}
