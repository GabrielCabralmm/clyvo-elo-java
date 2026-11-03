package com.clyvoelo.painel.controller;

import com.clyvoelo.painel.model.EventoClinico;
import com.clyvoelo.painel.model.Pet;
import com.clyvoelo.painel.security.UsuarioAutenticado;
import com.clyvoelo.painel.service.AreaTutorService;
import com.clyvoelo.painel.service.ClyvoAiClient;
import com.clyvoelo.painel.service.EventoClinicoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;

import java.util.List;

/**
 * Demonstração integrada: pet e evento vêm do banco do painel Java, autorizações
 * via AreaTutorService, sensores são valores simulados informados em formulário.
 */
@Controller
@RequestMapping("/minha-area/pets")
public class OrientacaoIaTutorController {

    private final AreaTutorService areaTutorService;
    private final EventoClinicoService eventoClinicoService;
    private final ClyvoAiClient clyvoAiClient;

    public OrientacaoIaTutorController(AreaTutorService areaTutorService,
                                      EventoClinicoService eventoClinicoService,
                                      ClyvoAiClient clyvoAiClient) {
        this.areaTutorService = areaTutorService;
        this.eventoClinicoService = eventoClinicoService;
        this.clyvoAiClient = clyvoAiClient;
    }

    @GetMapping("/{id}/orientacao-ia")
    public String formulario(@PathVariable Long id, @AuthenticationPrincipal UsuarioAutenticado usuario,
                             Model model) {
        Pet pet = areaTutorService.petDoTutor(id, usuario.getId());
        model.addAttribute("pet", pet);
        return "tutor/orientacao-ia";
    }

    @PostMapping("/{id}/orientacao-ia")
    public String gerar(@PathVariable Long id, @AuthenticationPrincipal UsuarioAutenticado usuario,
                        @RequestParam double nivelAgua, @RequestParam double nivelRacao,
                        @RequestParam double temperatura, @RequestParam double umidade, Model model) {
        // Verificação de posse ANTES de ler histórico clínico ou consumir IA.
        Pet pet = areaTutorService.petDoTutor(id, usuario.getId());
        model.addAttribute("pet", pet);
        model.addAttribute("agua", nivelAgua);
        model.addAttribute("racao", nivelRacao);
        model.addAttribute("temperatura", temperatura);
        model.addAttribute("umidade", umidade);
        try {
            List<EventoClinico> historico = eventoClinicoService.historicoDoPet(id);
            EventoClinico ultimo = historico.isEmpty() ? null : historico.get(0);
            model.addAttribute("resultado", clyvoAiClient.analisarPet(pet, ultimo,
                    nivelAgua, nivelRacao, temperatura, umidade));
        } catch (IllegalArgumentException ex) {
            model.addAttribute("erroIntegracao", ex.getMessage());
        } catch (IllegalStateException | RestClientException ex) {
            // Nenhum segredo/token ou exceção interna exposta ao tutor.
            model.addAttribute("erroIntegracao", "Não foi possível obter a orientação agora. "
                    + "Os alertas e orientações da clínica permanecem disponíveis.");
        }
        return "tutor/orientacao-ia";
    }
}
