package com.clyvoelo.painel.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Map;
import java.util.LinkedHashMap;
import com.clyvoelo.painel.model.Pet;
import com.clyvoelo.painel.model.EventoClinico;
import java.util.Set;

/**
 * Integração servidor-a-servidor com o serviço Python da disciplina IoT/IA.
 * Os três cenários são sintéticos e não representam as leituras reais dos pets do painel.
 */
@Service
public class ClyvoAiClient {

    private static final Set<String> CENARIOS = Set.of("normal", "alerta", "pos_operatorio");
    private final RestClient http;
    private final String urlBase;
    private final String token;

    public ClyvoAiClient(@Value("${CLYVO_AI_URL:http://127.0.0.1:8000}") String urlBase,
                         @Value("${CLYVO_AI_TOKEN:}") String token) {
        this.urlBase = urlBase.replaceAll("/+$", "");
        this.token = token;
        SimpleClientHttpRequestFactory requests = new SimpleClientHttpRequestFactory();
        requests.setConnectTimeout(Duration.ofSeconds(5));
        requests.setReadTimeout(Duration.ofSeconds(40));
        this.http = RestClient.builder().requestFactory(requests).build();
    }

    /**
     * Integração real com dados autorizados do banco Java para pet e evento clínico.
     * Os dados de sensores são informados EXPLICITAMENTE para demonstração de IA,
     * não constituem leitura física de dispositivo IoT.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> analisarPet(Pet pet, EventoClinico evento,
            double agua, double racao, double temperatura, double umidade) {
        if (pet.getPesoKg() == null || pet.getPesoKg().doubleValue() <= 0) {
            throw new IllegalArgumentException("Informe o peso do pet no cadastro antes de analisar.");
        }
        if (agua < 0 || agua > 100 || racao < 0 || racao > 100
                || umidade < 0 || umidade > 100 || temperatura < -20 || temperatura > 80
                || !Double.isFinite(agua) || !Double.isFinite(racao)
                || !Double.isFinite(temperatura) || !Double.isFinite(umidade)) {
            throw new IllegalArgumentException("Leituras de demonstração fora da faixa permitida.");
        }

        Map<String, Object> dadosPet = new LinkedHashMap<>();
        dadosPet.put("id_pet", pet.getId());
        dadosPet.put("nome", pet.getNome());
        dadosPet.put("especie", pet.getEspecie().name());
        dadosPet.put("raca", pet.getRaca() == null || pet.getRaca().isBlank() ? "Não informada" : pet.getRaca());
        dadosPet.put("peso_kg", pet.getPesoKg());

        Map<String, Object> sensores = Map.of("nivel_agua", agua, "nivel_racao", racao,
                "temperatura", temperatura, "umidade", umidade);
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("pet", dadosPet);
        corpo.put("sensores", sensores);
        if (evento != null) {
            Map<String, String> clinico = new LinkedHashMap<>();
            clinico.put("tipo", evento.getTipoEvento().name());
            String descricao = evento.getDescricao() == null || evento.getDescricao().isBlank()
                    ? evento.getTipoEvento().getDescricao() : evento.getDescricao();
            clinico.put("descricao", descricao.substring(0, Math.min(250, descricao.length())));
            clinico.put("status", evento.getStatus().name());
            corpo.put("evento_clinico", clinico);
        }
        return enviarAnalise(corpo, true);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> enviarAnalise(Map<String, Object> dados, boolean comIa) {
        Map<String, Object> resultado = http.post()
                .uri(urlBase + (comIa ? "/api/analisar" : "/api/alertas"))
                .contentType(MediaType.APPLICATION_JSON)
                .headers(cabecalhos -> {
                    if (!token.isBlank()) {
                        cabecalhos.set("X-CLYVO-TOKEN", token);
                    }
                })
                .body(dados)
                .retrieve()
                .body(Map.class);
        if (resultado == null) {
            throw new IllegalStateException("O serviço de IA retornou resposta vazia.");
        }
        return resultado;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> analisarCenario(String cenario, boolean comIa) {
        if (!CENARIOS.contains(cenario)) {
            throw new IllegalArgumentException("Cenário inválido.");
        }
        // Dados controlados: o payload vem do mesmo serviço Python; não é dado do banco Java.
        Map<String, Object> dados = http.get()
                .uri(urlBase + "/api/cenarios/" + cenario)
                .retrieve()
                .body(Map.class);
        if (dados == null) {
            throw new IllegalStateException("O serviço de IA não forneceu dados.");
        }

        return enviarAnalise(dados, comIa);
    }
}
