package com.devstormlife.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devstormlife.api.entities.Subestacao;
import com.devstormlife.api.repositories.SubestacaoRepository;

@Service
public class SubestacaoService {
	
	@Autowired
	private SubestacaoRepository repository;
	
	public List<Subestacao> listarTodos(){
		return repository.findAll();
	}
	
	public Optional<Subestacao> buscarPorId(Long id) {
		return repository.findById(id);
	}
	
	public Subestacao salvar (Subestacao subestacao) {
		return repository.save(subestacao);
	}
	
	public Subestacao atualizar (Long id, Subestacao subestacaoAlterada) {
		Optional<Subestacao> subestacaoExistente = buscarPorId(id);
		
		if (subestacaoExistente.isPresent()) {
			
			Subestacao atualizado = subestacaoExistente.get();
			
			atualizado.setNome(subestacaoAlterada.getNome());
			atualizado.setFuncionarios(subestacaoAlterada.getFuncionarios());
			
			return repository.save(atualizado);
		}
		return null;
	}
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}
}
