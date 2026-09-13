package com.clyvoelo.painel.controller;

import com.clyvoelo.painel.model.Funcionario;
import com.clyvoelo.painel.repository.FuncionarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    public FuncionarioController(FuncionarioRepository funcionarioRepository, PasswordEncoder passwordEncoder) {
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("funcionarios", funcionarioRepository.findAll());
        return "funcionarios/lista";
    }

    @GetMapping("/novo")
    public String formNovo(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "funcionarios/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Funcionario funcionario, BindingResult resultado,
                          @RequestParam(required = false) String senha) {
        if (resultado.hasErrors()) {
            return "funcionarios/form";
        }
        if (senha != null && !senha.isBlank()) {
            funcionario.setSenhaHash(passwordEncoder.encode(senha));
        } else if (funcionario.getId() != null) {
            Funcionario existente = funcionarioRepository.findById(funcionario.getId()).orElseThrow();
            funcionario.setSenhaHash(existente.getSenhaHash());
            funcionario.setDataCadastro(existente.getDataCadastro());
        }
        if (funcionario.getDataCadastro() == null) {
            funcionario.setDataCadastro(LocalDate.now());
        }
        funcionarioRepository.save(funcionario);
        return "redirect:/funcionarios";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        funcionarioRepository.deleteById(id);
        return "redirect:/funcionarios";
    }
}
