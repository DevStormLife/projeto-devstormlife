package com.devstormlife.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devstormlife.api.entities.CartaoAcesso;
import com.devstormlife.api.services.CartaoAcessoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cartao")
@CrossOrigin("*")
public class CartaoAcessoController {
	
	@Autowired
	private CartaoAcessoService service;
	
	@GetMapping
	public ResponseEntity<List<CartaoAcesso>> listar() {
		return ResponseEntity.ok(service.listarTodos());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CartaoAcesso> buscar(@PathVariable Long id) {
		Optional<CartaoAcesso> cartaoAcesso = service.buscarPorId(id);
		
		if (cartaoAcesso != null) {
			return ResponseEntity.ok(cartaoAcesso.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<CartaoAcesso> criar(@Valid @RequestBody CartaoAcesso cartaoAcesso) {
		CartaoAcesso novoCartao = service.salvar(cartaoAcesso);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(novoCartao);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CartaoAcesso> atualizar(@PathVariable Long id, @RequestBody CartaoAcesso cartaoAcesso) {
		CartaoAcesso cartaoAtualizado = service.atualizar(id, cartaoAcesso);
		
		if (cartaoAtualizado != null) {
			return ResponseEntity.ok(cartaoAtualizado);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletar(@PathVariable Long id) {
		Optional<CartaoAcesso> cartaoAcesso = service.buscarPorId(id);
		
		if(cartaoAcesso.isPresent()) {
			
			service.deletar(id);
			
			return ResponseEntity.status(HttpStatus.OK).body("Sucesso: O cartão de acesso foi excluído permanentemente!");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: Não foi possível deletar. O cartão de acesso com ID " + id + " não foi encontrado");
	}
}
