package com.clyvoelo.painel.controller;

import com.clyvoelo.painel.model.Alerta;
import com.clyvoelo.painel.model.Funcionario;
import com.clyvoelo.painel.repository.AlertaRepository;
import com.clyvoelo.painel.repository.FuncionarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/alertas")
public class AlertaController {

    private final AlertaRepository alertaRepository;
    private final FuncionarioRepository funcionarioRepository;

    public AlertaController(AlertaRepository alertaRepository, FuncionarioRepository funcionarioRepository) {
        this.alertaRepository = alertaRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @GetMapping
    public String listarAbertos(Model model) {
        model.addAttribute("alertas", alertaRepository.findByStatusOrderByDataCriacaoAsc("ABERTO"));
        return "alertas/lista";
    }

    @GetMapping("/{id}/resolver")
    public String formResolver(@PathVariable Long id, Model model) {
        model.addAttribute("alerta", alertaRepository.findById(id).orElseThrow());
        return "alertas/resolver";
    }

    @PostMapping("/{id}/resolver")
    public String confirmarResolucao(@PathVariable Long id,
                                      @RequestParam String notaResolucao,
                                      Authentication authentication) {
        Alerta alerta = alertaRepository.findById(id).orElseThrow();
        Funcionario doutor = funcionarioRepository.findByEmail(authentication.getName()).orElseThrow();

        alerta.setNotaResolucao(notaResolucao);
        alerta.setFuncionarioResolveu(doutor);
        alerta.setStatus("RESOLVIDO");
        alerta.setDataResolucao(LocalDate.now());
        alertaRepository.save(alerta);

        return "redirect:/alertas";
    }
}
