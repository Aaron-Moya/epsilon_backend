package com.aaron.epsilon_backend.modelos.controladores;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aaron.epsilon_backend.modelos.dto.UsuarioLoginDTO;
import com.aaron.epsilon_backend.modelos.entidades.Usuarios;
import com.aaron.epsilon_backend.utilidades.ConverterUsuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuariosRestController usuariosRestController;

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String correo, @RequestParam String password) throws NoSuchAlgorithmException {
        Usuarios user = usuariosRestController.comprobarCredenciales(correo, password);
        if(user !=null)
        	return ResponseEntity.ok().body(ConverterUsuario.convertirUsuarioLogin(user));
        else
        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("usuario y/o contraseña incorrectos");
    }
}