package com.devstormlife.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devstormlife.api.entities.CartaoAcesso;
import com.devstormlife.api.repositories.CartaoAcessoRepository;

@Service
public class CartaoAcessoService {
	
	@Autowired
	private CartaoAcessoRepository repository;
	
	public List<CartaoAcesso> listarTodos(){
		return repository.findAll();
	}
	
	public Optional<CartaoAcesso> buscarPorId(Long id) {
		return repository.findById(id);
	}
	
	public CartaoAcesso salvar (CartaoAcesso cartaoAcesso) {
		return repository.save(cartaoAcesso);
	}
	
	public CartaoAcesso atualizar(Long id, CartaoAcesso cartaoAlterado) {
		Optional<CartaoAcesso> cartaoExistente = buscarPorId(id);
		
		if(cartaoExistente.isPresent()) {
			
			CartaoAcesso atualizado = cartaoExistente.get();
			
			atualizado.setCodigoSerial(cartaoAlterado.getCodigoSerial());
			atualizado.setFuncionario(cartaoAlterado.getFuncionario());
			atualizado.setSubstacao(cartaoAlterado.getSubstacao());
			
			return repository.save(atualizado);
		}
		return null;
	}
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}
}
