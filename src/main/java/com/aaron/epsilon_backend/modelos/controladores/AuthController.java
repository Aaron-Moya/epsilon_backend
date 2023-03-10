package com.aaron.epsilon_backend.modelos.controladores;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.utilidades.ConverterUsuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuariosRestController usuariosRestController;

    @GetMapping("/login")
    @Operation(
    		summary = "Comprueba las credenciales de un usuario", description = "Comprueba las credenciales de un usuario",
    		responses = {
    				@ApiResponse(
    						responseCode = "200",
    						description = "OK",
    						content = @Content()),
    				@ApiResponse(
    						responseCode = "401",
    						description = "Usuario y/o contraseña incorrectos",
    						content = @Content()),
					@ApiResponse(
    						responseCode = "500",
    						description = "Error en la base de datos",
    						content = @Content())
    		})
    public ResponseEntity<?> login(@RequestParam String correo, @RequestParam String password) {
        Usuarios user = usuariosRestController.comprobarCredenciales(correo, password);
        if(user !=null)
        	return ResponseEntity.ok().body(ConverterUsuario.convertirUsuarioLogin(user));
        else
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("usuario y/o contraseña incorrectos");
    }
}