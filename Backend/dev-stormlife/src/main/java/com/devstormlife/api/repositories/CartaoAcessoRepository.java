package com.devstormlife.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devstormlife.api.entities.CartaoAcesso;

@Repository
public interface CartaoAcessoRepository extends JpaRepository<CartaoAcesso, Long>{

}
