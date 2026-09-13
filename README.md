# CLYVO Elo — Painel da Clínica

Aplicação web em Spring Boot desenvolvida para o Challenge FIAP 2026 (CLYVO VET), como entrega da disciplina de Java Advanced (Sprint 3).

## Sobre o projeto

O Painel da Clínica é o sistema de uso interno do CLYVO VET, usado pela administração e pelo corpo clínico para gerenciar pacientes, a equipe, e o acompanhamento clínico do dia a dia — cadastro de pets, atendimento de eventos clínicos pendentes, e resolução de alertas.

## Stack técnica

- Java 21, Spring Boot 4.1
- Thymeleaf como camada de visualização (frontend)
- Spring Security para autenticação e controle de acesso por perfil
- Spring Data JPA + Oracle Database
- Flyway para controle de versão do schema do banco

## Como executar

Pré-requisitos: Java 21 e Maven (ou uma IDE com suporte a Maven, como IntelliJ).

```
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`. As credenciais de conexão ao Oracle já estão preenchidas em `src/main/resources/application.properties`. Na primeira execução, o Flyway cria automaticamente as tabelas do projeto e insere os dados de demonstração — não é necessário nenhum passo manual no banco.

### Acesso de demonstração

| Perfil | Email | Senha |
|---|---|---|
| Administrador | renata.souza@clyvovet.com | clyvo123 |
| Doutor | ana.ferreira@clyvovet.com | clyvo123 |

## Perfis e permissões

O controle de acesso é implementado via `SecurityFilterChain`, com rotas protegidas por perfil:

| Área | Administrador | Doutor |
|---|---|---|
| Pets (cadastrar, editar, excluir) | Sim | Não (403) |
| Equipe / funcionários (cadastrar, excluir) | Sim | Não (403) |
| Eventos clínicos pendentes (atender) | Não (403) | Sim |
| Alertas abertos (resolver) | Não (403) | Sim |

## Fluxos completos implementados

Além do CRUD de Pets e de Funcionários, a aplicação implementa dois fluxos de negócio completos:

1. **Atendimento de evento clínico**: o Doutor consulta a lista de eventos pendentes, abre um evento específico, registra o que foi realizado no atendimento, e o evento é marcado como concluído com o nome do profissional responsável e a data.
2. **Resolução de alerta**: o Doutor consulta os alertas em aberto (originados de sensores IoT ou registro manual), abre um alerta, registra uma nota de resolução, e o alerta é marcado como resolvido.

## Estrutura do projeto

```
src/main/java/com/clyvoelo/painel/
  ClyvoEloPainelApplication.java   ponto de entrada
  config/
    SecurityConfig.java             Spring Security: login, CSRF, autorização por rota
  service/
    FuncionarioUserDetailsService.java   carrega o usuário autenticado a partir do banco
  model/
    Funcionario.java, Pet.java, EventoClinico.java, Alerta.java
  repository/
    interfaces Spring Data JPA para cada entidade
  controller/
    PainelController.java           login e dashboard
    PetController.java              CRUD de pets
    FuncionarioController.java      CRUD de funcionários
    EventoClinicoController.java    fluxo de atendimento
    AlertaController.java           fluxo de resolução

src/main/resources/
  application.properties            configuração da aplicação e conexão com o Oracle
  db/migration/
    V1__criar_tabelas.sql           schema (tabelas, constraints)
    V2__dados_demo.sql              dados de demonstração
  templates/                        views Thymeleaf, organizadas por área (pets/, eventos/, alertas/, funcionarios/)
  static/css/                       identidade visual do CLYVO Elo
```

## Banco de dados

O schema é criado e versionado inteiramente pelo Flyway, a partir dos arquivos em `db/migration/`. Todas as tabelas usam o prefixo `painel_` (`painel_funcionario`, `painel_pet`, `painel_evento_clinico`, `painel_alerta`).
