package com.devstormlife.api.services;
	import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	    
	    @Autowired
	    private PasswordEncoder passwordEncoder; // Injeta o BCryptPasswordEncoder configurado na SecurityConfig

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

	            // Atualiza os dados básicos do formulário
	            atualizado.setNome(funcionarioAlterado.getNome());
	            atualizado.setEmail(funcionarioAlterado.getEmail());
	            atualizado.setCpf(funcionarioAlterado.getCpf());
	            atualizado.setCargo(funcionarioAlterado.getCargo());
	            
	            // Se a senha foi preenchida no request, codifica com BCrypt.
	            // Se veio vazia ou nula (como no Modal de edição), MANTÉM a senha criptografada antiga!
	            if (funcionarioAlterado.getSenha() != null && !funcionarioAlterado.getSenha().trim().isEmpty()) {
	                atualizado.setSenha(passwordEncoder.encode(funcionarioAlterado.getSenha()));
	            }

	            // Se veio subestação, atualiza; caso contrário mantém a atual
	            if (funcionarioAlterado.getSubestacao() != null) {
	                atualizado.setSubestacao(funcionarioAlterado.getSubestacao());
	            }

	            return repository.save(atualizado);
	        }

	        return null;
	    }

	 
	    public void deletar(Long id) {
	        repository.deleteById(id);
	    }
	}

