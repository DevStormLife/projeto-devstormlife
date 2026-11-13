package com.devstormlife.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devstormlife.api.entities.Usuario;
import com.devstormlife.api.services.UsuarioService;


@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;
    
    @PostMapping("/cadastro")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario){
        
        Usuario usuarioSalvo = service.salvar(usuario);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody Usuario usuarioLogin, HttpServletRequest request){
        
        Usuario usuario = service.login(usuarioLogin.getEmail(), usuarioLogin.getSenha());
        
        HttpSession old = request.getSession(false);
        if (old != null) old.invalidate();
        HttpSession session = request.getSession(true);
        session.setAttribute("stormUserId", usuario.getId());
        session.setAttribute("stormUserRole", usuario.getTipo());
        return ResponseEntity.ok(usuario);
    }
    
}
