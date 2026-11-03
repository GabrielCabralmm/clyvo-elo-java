package com.clyvoelo.painel.controller;

import com.clyvoelo.painel.service.ClyvoAiClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/ecossistema/ia")
public class IntegracaoIaController {

    private final ClyvoAiClient clyvoAiClient;

    public IntegracaoIaController(ClyvoAiClient clyvoAiClient) {
        this.clyvoAiClient = clyvoAiClient;
    }

    private void preparar(Model model, String cenario) {
        Map<String, String> opcoes = new LinkedHashMap<>();
        opcoes.put("normal", "Lara — Rotina normal");
        opcoes.put("alerta", "Zakaria — Alertas ambientais");
        opcoes.put("pos_operatorio", "Bolt — Pós-operatório");
        model.addAttribute("opcoes", opcoes);
        model.addAttribute("cenarioSelecionado", cenario);
    }

    @GetMapping
    public String formulario(Model model) {
        preparar(model, "alerta");
        return "ecossistema-ia";
    }

    @PostMapping("/analisar")
    public String analisar(@RequestParam(defaultValue = "alerta") String cenario,
                           @RequestParam(defaultValue = "false") boolean comIa,
                           Model model) {
        preparar(model, cenario);
        try {
            // Chamada a FastAPI feita pelo backend, nunca pelo navegador do tutor.
            model.addAttribute("resultado", clyvoAiClient.analisarCenario(cenario, comIa));
        } catch (IllegalArgumentException | IllegalStateException | RestClientException erro) {
            // Não vazar chaves de API, credenciais nem detalhes do provedor.
            model.addAttribute("erroIntegracao", "O serviço de IA está indisponível ou recusou a análise. "
                    + "Confirme se a API Python está em execução e configurada.");
        }
        return "ecossistema-ia";
    }
}
