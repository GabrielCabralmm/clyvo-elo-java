package com.clyvoelo.painel.service;

import com.clyvoelo.painel.model.Funcionario;
import com.clyvoelo.painel.repository.FuncionarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioUserDetailsService implements UserDetailsService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioUserDetailsService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email ou senha inválidos"));

        return User.builder()
                .username(funcionario.getEmail())
                .password(funcionario.getSenhaHash())
                .roles(funcionario.getPerfil())
                .build();
    }
}
