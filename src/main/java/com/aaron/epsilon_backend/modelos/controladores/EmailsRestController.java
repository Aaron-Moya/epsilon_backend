package com.aaron.epsilon_backend.modelos.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/email")
public class EmailsRestController {

	@Autowired
    private JavaMailSender mailSender;
	
    @Operation(
    		summary = "Envia un correo al email que se le pasa por parámetro", 
    		description = "Envia un correo al email que se le pasa por parámetro"
    		)
	@GetMapping("/registro")
    public void crearEmail(@RequestParam String email, @RequestParam String texto, @RequestParam String asunto) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(email);
        mensaje.setText(texto);
        mensaje.setSubject(asunto);
        mailSender.send(mensaje);
    }
}
