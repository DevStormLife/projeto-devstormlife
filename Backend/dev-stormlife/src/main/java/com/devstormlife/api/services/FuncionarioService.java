package com.devstormlife.api.services;
	import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.devstormlife.api.enums.Cargo;

import com.devstormlife.api.entities.Funcionario;
import com.devstormlife.api.entities.Usuario;
import com.devstormlife.api.repositories.FuncionarioRepository;
import com.devstormlife.api.repositories.UsuarioRepository;

	@Service
	public class FuncionarioService {

	    @Autowired
	    private FuncionarioRepository repository;
	    
	    @Autowired
	    private UsuarioService usuarioService;

	    @Autowired
	    private UsuarioRepository usuarioRepository;
	    
	    @Autowired
	    private PasswordEncoder passwordEncoder; // Injeta o BCryptPasswordEncoder configurado na SecurityConfig

	    public List<Funcionario> listarTodos() {
	        return repository.findAll();
	    }

	    public Optional<Funcionario> buscarPorId(Long id) {
	        return repository.findById(id);
	    }

	    @Transactional
	    public Funcionario salvar(Funcionario funcionario) {
	    	
	    	Usuario usuario = new Usuario();
	    	usuario.setEmail(funcionario.getEmail());
	    	usuario.setSenha(funcionario.getSenha());
	    	usuario.setTipo("OPERADOR");
	    	
	    	usuarioService.salvar(usuario);
	    	funcionario.setId(null);
	    	funcionario.setCargo(Cargo.OPERADOR);
	    	funcionario.setSenha(passwordEncoder.encode(funcionario.getSenha()));
	        return repository.save(funcionario);
	    }
	    
	    @Transactional
	    public Funcionario atualizar(Long id, Funcionario funcionarioAlterado) {
	        Optional<Funcionario> funcionarioExistente = buscarPorId(id);

	        if (funcionarioExistente.isPresent()) {
	            Funcionario atualizado = funcionarioExistente.get();
	            Usuario login = usuarioRepository.findByEmail(atualizado.getEmail()).orElse(null);

	            // Atualiza os dados básicos do formulário
	            atualizado.setNome(funcionarioAlterado.getNome());
	            atualizado.setEmail(funcionarioAlterado.getEmail());
	            atualizado.setCpf(funcionarioAlterado.getCpf());
	            atualizado.setCargo(funcionarioAlterado.getCargo());
	            if (login != null) {
	                login.setEmail(funcionarioAlterado.getEmail());
	                usuarioRepository.save(login);
	            }
	            
	            // Se a senha foi preenchida no request, codifica com BCrypt.
	            // Se veio vazia ou nula (como no Modal de edição), MANTÉM a senha criptografada antiga!
	            if (funcionarioAlterado.getSenha() != null && !funcionarioAlterado.getSenha().trim().isEmpty()) {
	                atualizado.setSenha(passwordEncoder.encode(funcionarioAlterado.getSenha()));
	                if (login != null) {
	                    login.setSenha(passwordEncoder.encode(funcionarioAlterado.getSenha()));
	                    usuarioRepository.save(login);
	                }
	            }

	            // Se veio subestação, atualiza; caso contrário mantém a atual
	            if (funcionarioAlterado.getSubestacao() != null) {
	                atualizado.setSubestacao(funcionarioAlterado.getSubestacao());
	            }

	            return repository.save(atualizado);
	        }

	        return null;
	    }

	 
	    @Transactional
	    public void deletar(Long id) {
	        repository.findById(id).ifPresent(funcionario -> {
	            repository.delete(funcionario);
	            usuarioRepository.findByEmail(funcionario.getEmail()).ifPresent(usuarioRepository::delete);
	        });
	    }
	}

