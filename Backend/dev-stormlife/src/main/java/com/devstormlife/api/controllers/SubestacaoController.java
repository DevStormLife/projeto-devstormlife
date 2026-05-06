package com.devstormlife.api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.devstormlife.api.entities.Subestacao;
import com.devstormlife.api.services.SubestacaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/subestacoes")
@CrossOrigin("*")
public class SubestacaoController {
	
	@Autowired
	private SubestacaoService service;
	
	@GetMapping
	public ResponseEntity<List<Subestacao>> listar() {
		return ResponseEntity.ok(service.listarTodos());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Subestacao> buscar(@PathVariable Long id) {
		Optional<Subestacao> subestacao = service.buscarPorId(id);
		
		if (subestacao != null) {
			return ResponseEntity.ok(subestacao.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public Subestacao criar(@Valid @RequestBody Subestacao subestacao) {
		return service.salvar(subestacao);
	}
	
	@PutMapping("/{id}")
	public Subestacao atualizar(@PathVariable Long id, @RequestBody Subestacao subestacao) {
		return service.atualizar(id, subestacao);
	}
	
	@DeleteMapping("/{id}")
	public void deletar(@PathVariable Long id) {
		service.deletar(id);
	}
}
