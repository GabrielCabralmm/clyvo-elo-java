package com.clyvoelo.painel.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "painel_alerta")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pet", nullable = false)
    private Pet pet;

    @Column(nullable = false, length = 30)
    private String origem;

    @Column(nullable = false, length = 20)
    private String prioridade;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    @Column(name = "nota_resolucao", length = 500)
    private String notaResolucao;

    @ManyToOne
    @JoinColumn(name = "id_funcionario_resolveu")
    private Funcionario funcionarioResolveu;

    @Column(name = "data_resolucao")
    private LocalDate dataResolucao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getNotaResolucao() { return notaResolucao; }
    public void setNotaResolucao(String notaResolucao) { this.notaResolucao = notaResolucao; }

    public Funcionario getFuncionarioResolveu() { return funcionarioResolveu; }
    public void setFuncionarioResolveu(Funcionario funcionarioResolveu) { this.funcionarioResolveu = funcionarioResolveu; }

    public LocalDate getDataResolucao() { return dataResolucao; }
    public void setDataResolucao(LocalDate dataResolucao) { this.dataResolucao = dataResolucao; }
}
