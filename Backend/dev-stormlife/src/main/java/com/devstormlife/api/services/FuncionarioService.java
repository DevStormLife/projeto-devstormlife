package com.devstormlife.api.services;
	import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devstormlife.api.entities.Funcionario;
import com.devstormlife.api.entities.Usuario;
import com.devstormlife.api.repositories.FuncionarioRepository;

	@Service
	public class FuncionarioService {

	    @Autowired
	    private FuncionarioRepository repository;
	    
	    @Autowired
	    private UsuarioService usuarioService;

	    public List<Funcionario> listarTodos() {
	        return repository.findAll();
	    }

	    public Optional<Funcionario> buscarPorId(Long id) {
	        return repository.findById(id);
	    }

	    public Funcionario salvar(Funcionario funcionario) {
	    	
	    	Usuario usuario = new Usuario();
	    	usuario.setEmail(funcionario.getEmail());
	    	usuario.setSenha(funcionario.getSenha());
	    	usuario.setTipo("Funcionário");
	    	
	    	usuarioService.salvar(usuario);
	    	
	        return repository.save(funcionario);
	    }

	    public Funcionario atualizar(Long id, Funcionario funcionarioAlterado) {
	        Optional<Funcionario> funcionarioExistente = buscarPorId(id);

	        if (funcionarioExistente.isPresent()) {
	            Funcionario atualizado = funcionarioExistente.get();

	            atualizado.setNome(funcionarioAlterado.getNome());
	            atualizado.setEmail(funcionarioAlterado.getEmail());
	            atualizado.setCpf(funcionarioAlterado.getCpf());
	            atualizado.setCargo(funcionarioAlterado.getCargo());
	           
	            atualizado.setCartaoAcesso(funcionarioAlterado.getCartaoAcesso());

	            return repository.save(atualizado);
	        }

	        return null;
	    }

	 
	    public void deletar(Long id) {
	        repository.deleteById(id);
	    }
	}

