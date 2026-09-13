package com.clyvoelo.painel.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "painel_evento_clinico")
public class EventoClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pet", nullable = false)
    private Pet pet;

    @Column(name = "tipo_evento", nullable = false, length = 30)
    private String tipoEvento;

    @Column(length = 500)
    private String descricao;

    @Column(name = "data_evento")
    private LocalDate dataEvento;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "observacao_atendimento", length = 500)
    private String observacaoAtendimento;

    @ManyToOne
    @JoinColumn(name = "id_funcionario_atendeu")
    private Funcionario funcionarioAtendeu;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataEvento() { return dataEvento; }
    public void setDataEvento(LocalDate dataEvento) { this.dataEvento = dataEvento; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservacaoAtendimento() { return observacaoAtendimento; }
    public void setObservacaoAtendimento(String observacaoAtendimento) { this.observacaoAtendimento = observacaoAtendimento; }

    public Funcionario getFuncionarioAtendeu() { return funcionarioAtendeu; }
    public void setFuncionarioAtendeu(Funcionario funcionarioAtendeu) { this.funcionarioAtendeu = funcionarioAtendeu; }
}
