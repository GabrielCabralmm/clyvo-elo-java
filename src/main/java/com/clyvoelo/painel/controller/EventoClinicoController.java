package com.clyvoelo.painel.controller;

import com.clyvoelo.painel.model.EventoClinico;
import com.clyvoelo.painel.model.Funcionario;
import com.clyvoelo.painel.repository.EventoClinicoRepository;
import com.clyvoelo.painel.repository.FuncionarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/eventos")
public class EventoClinicoController {

    private final EventoClinicoRepository eventoClinicoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public EventoClinicoController(EventoClinicoRepository eventoClinicoRepository, FuncionarioRepository funcionarioRepository) {
        this.eventoClinicoRepository = eventoClinicoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @GetMapping
    public String listarPendentes(Model model) {
        model.addAttribute("eventos", eventoClinicoRepository.findByStatusOrderByDataEventoAsc("PENDENTE"));
        return "eventos/lista";
    }

    @GetMapping("/{id}/atender")
    public String formAtender(@PathVariable Long id, Model model) {
        model.addAttribute("evento", eventoClinicoRepository.findById(id).orElseThrow());
        return "eventos/atender";
    }

    @PostMapping("/{id}/atender")
    public String confirmarAtendimento(@PathVariable Long id,
                                        @RequestParam String observacaoAtendimento,
                                        Authentication authentication) {
        EventoClinico evento = eventoClinicoRepository.findById(id).orElseThrow();
        Funcionario doutor = funcionarioRepository.findByEmail(authentication.getName()).orElseThrow();

        evento.setObservacaoAtendimento(observacaoAtendimento);
        evento.setFuncionarioAtendeu(doutor);
        evento.setStatus("CONCLUIDO");
        eventoClinicoRepository.save(evento);

        return "redirect:/eventos";
    }
}
