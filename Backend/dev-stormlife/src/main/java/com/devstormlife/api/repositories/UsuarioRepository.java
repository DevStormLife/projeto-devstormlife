package com.devstormlife.api.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devstormlife.api.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    Optional<Usuario> findByEmail(String email);
}