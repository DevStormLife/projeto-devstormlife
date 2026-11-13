package com.devstormlife.api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.devstormlife.api.entities.Usuario;
import com.devstormlife.api.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    
    @Autowired
    private  BCryptPasswordEncoder password;
    
    public Usuario salvar(Usuario usuario) {
        
        Optional<Usuario> usuarioExistente = buscarPorEmail(usuario.getEmail());
        
        if(usuarioExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com este email.");
        }
        
        usuario.setId(null);
        usuario.setTipo("OPERADOR");
        String senhaCriptografada = password.encode(usuario.getSenha());
        
        usuario.setSenha(senhaCriptografada);
        
        return repository.save(usuario);
    }
    
    public Optional<Usuario> buscarPorEmail(String email){
        return repository.findByEmail(email);
    }
    public Usuario login(String email, String senha) {
        
        Usuario usuario = repository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        
        boolean senhaValida = senha != null && password.matches(senha, usuario.getSenha());
        
        if(!senhaValida){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        
        return usuario;
    }
}
