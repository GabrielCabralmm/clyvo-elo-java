package com.clyvoelo.painel.controller;

import com.clyvoelo.painel.repository.AlertaRepository;
import com.clyvoelo.painel.repository.EventoClinicoRepository;
import com.clyvoelo.painel.repository.PetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class PainelController {

    private final PetRepository petRepository;
    private final EventoClinicoRepository eventoClinicoRepository;
    private final AlertaRepository alertaRepository;

    public PainelController(PetRepository petRepository, EventoClinicoRepository eventoClinicoRepository, AlertaRepository alertaRepository) {
        this.petRepository = petRepository;
        this.eventoClinicoRepository = eventoClinicoRepository;
        this.alertaRepository = alertaRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String dashboard(Model model, Authentication authentication) {
        model.addAttribute("nomeUsuario", authentication.getName());
        model.addAttribute("ehAdmin", authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        model.addAttribute("ehDoutor", authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOUTOR")));
        model.addAttribute("totalPets", petRepository.count());
        model.addAttribute("eventosPendentes", eventoClinicoRepository.findByStatusOrderByDataEventoAsc("PENDENTE").size());
        model.addAttribute("alertasAbertos", alertaRepository.findByStatusOrderByDataCriacaoAsc("ABERTO").size());
        return "dashboard";
    }
}
