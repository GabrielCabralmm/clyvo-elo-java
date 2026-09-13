package com.clyvoelo.painel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Entity
@Table(name = "painel_pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pet")
    private Long id;

    @NotBlank(message = "Informe o nome do pet")
    @Column(nullable = false, length = 80)
    private String nome;

    @NotBlank(message = "Selecione a especie")
    @Column(nullable = false, length = 20)
    private String especie;

    @Column(length = 80)
    private String raca;

    @Column(name = "peso_kg")
    private BigDecimal pesoKg;

    @NotBlank(message = "Informe o nome do tutor")
    @Column(name = "nome_tutor", nullable = false, length = 120)
    private String nomeTutor;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }

    public BigDecimal getPesoKg() { return pesoKg; }
    public void setPesoKg(BigDecimal pesoKg) { this.pesoKg = pesoKg; }

    public String getNomeTutor() { return nomeTutor; }
    public void setNomeTutor(String nomeTutor) { this.nomeTutor = nomeTutor; }
}