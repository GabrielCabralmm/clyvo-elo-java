CREATE TABLE painel_funcionario (
                                    id_funcionario NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                    nome VARCHAR2(120) NOT NULL,
                                    email VARCHAR2(150) NOT NULL UNIQUE,
                                    senha_hash VARCHAR2(255) NOT NULL,
                                    perfil VARCHAR2(20) NOT NULL,
                                    data_cadastro DATE DEFAULT SYSDATE NOT NULL,
                                    CONSTRAINT painel_ck_funcionario_perfil CHECK (perfil IN ('ADMIN', 'DOUTOR'))
);

CREATE TABLE painel_pet (
                            id_pet NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                            nome VARCHAR2(80) NOT NULL,
                            especie VARCHAR2(20) NOT NULL,
                            raca VARCHAR2(80),
                            peso_kg NUMBER(5,2),
                            nome_tutor VARCHAR2(120) NOT NULL,
                            CONSTRAINT painel_ck_pet_especie CHECK (especie IN ('CACHORRO', 'GATO'))
);

CREATE TABLE painel_evento_clinico (
                                       id_evento NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                       id_pet NUMBER NOT NULL,
                                       tipo_evento VARCHAR2(30) NOT NULL,
                                       descricao VARCHAR2(500),
                                       data_evento DATE DEFAULT SYSDATE NOT NULL,
                                       status VARCHAR2(20) DEFAULT 'PENDENTE' NOT NULL,
                                       observacao_atendimento VARCHAR2(500),
                                       id_funcionario_atendeu NUMBER,
                                       CONSTRAINT painel_ck_evento_tipo CHECK (tipo_evento IN ('VACINA','CHECKUP','POS_OPERATORIO','MEDICACAO','EXAME')),
                                       CONSTRAINT painel_ck_evento_status CHECK (status IN ('PENDENTE','CONCLUIDO','ATRASADO')),
                                       CONSTRAINT painel_fk_evento_pet FOREIGN KEY (id_pet) REFERENCES painel_pet(id_pet),
                                       CONSTRAINT painel_fk_evento_funcionario FOREIGN KEY (id_funcionario_atendeu) REFERENCES painel_funcionario(id_funcionario)
);

CREATE TABLE painel_alerta (
                               id_alerta NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               id_pet NUMBER NOT NULL,
                               origem VARCHAR2(30) NOT NULL,
                               prioridade VARCHAR2(20) NOT NULL,
                               descricao VARCHAR2(500) NOT NULL,
                               status VARCHAR2(20) DEFAULT 'ABERTO' NOT NULL,
                               data_criacao DATE DEFAULT SYSDATE NOT NULL,
                               nota_resolucao VARCHAR2(500),
                               id_funcionario_resolveu NUMBER,
                               data_resolucao DATE,
                               CONSTRAINT painel_ck_alerta_prioridade CHECK (prioridade IN ('BAIXA','MEDIA','ALTA')),
                               CONSTRAINT painel_ck_alerta_status CHECK (status IN ('ABERTO','RESOLVIDO')),
                               CONSTRAINT painel_fk_alerta_pet FOREIGN KEY (id_pet) REFERENCES painel_pet(id_pet),
                               CONSTRAINT painel_fk_alerta_funcionario FOREIGN KEY (id_funcionario_resolveu) REFERENCES painel_funcionario(id_funcionario)
);