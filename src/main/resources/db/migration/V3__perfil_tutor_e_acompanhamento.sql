-- Tutores
CREATE TABLE painel_tutor (
    id_tutor NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR2(120) NOT NULL,
    email VARCHAR2(150) NOT NULL UNIQUE,
    senha_hash VARCHAR2(255) NOT NULL,
    telefone VARCHAR2(20),
    data_cadastro DATE DEFAULT SYSDATE NOT NULL
);

INSERT INTO painel_tutor (nome, email, senha_hash)
SELECT nome_tutor, LOWER(REPLACE(TRIM(nome_tutor), ' ', '.')) || '@email.com', '$2b$10$nSigTEiBHI5Oqe4q0AJcAukZ9oUOjeYptHi51so4dIJIhorZ5LciS'
FROM (SELECT DISTINCT nome_tutor FROM painel_pet);

-- Vinculo do pet com o tutor
ALTER TABLE painel_pet ADD (id_tutor NUMBER, data_nascimento DATE);

UPDATE painel_pet p SET id_tutor = (SELECT t.id_tutor FROM painel_tutor t WHERE t.nome = p.nome_tutor);

ALTER TABLE painel_pet MODIFY (id_tutor NOT NULL);
ALTER TABLE painel_pet ADD CONSTRAINT painel_fk_pet_tutor FOREIGN KEY (id_tutor) REFERENCES painel_tutor(id_tutor);
ALTER TABLE painel_pet DROP COLUMN nome_tutor;

-- Atendimento de eventos clinicos
ALTER TABLE painel_evento_clinico ADD (data_atendimento DATE);

UPDATE painel_evento_clinico SET data_atendimento = data_evento WHERE status = 'CONCLUIDO';

-- Acompanhamento de alertas
ALTER TABLE painel_alerta DROP CONSTRAINT painel_ck_alerta_status;
ALTER TABLE painel_alerta ADD CONSTRAINT painel_ck_alerta_status CHECK (status IN ('ABERTO','EM_ACOMPANHAMENTO','RESOLVIDO'));
ALTER TABLE painel_alerta ADD (id_funcionario_responsavel NUMBER, id_tutor_relatou NUMBER);
ALTER TABLE painel_alerta ADD CONSTRAINT painel_fk_alerta_responsavel FOREIGN KEY (id_funcionario_responsavel) REFERENCES painel_funcionario(id_funcionario);
ALTER TABLE painel_alerta ADD CONSTRAINT painel_fk_alerta_tutor FOREIGN KEY (id_tutor_relatou) REFERENCES painel_tutor(id_tutor);

CREATE TABLE painel_alerta_acompanhamento (
    id_acompanhamento NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_alerta NUMBER NOT NULL,
    autor VARCHAR2(120) NOT NULL,
    anotacao VARCHAR2(500) NOT NULL,
    data_registro TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT painel_fk_acompanhamento_alerta FOREIGN KEY (id_alerta) REFERENCES painel_alerta(id_alerta)
);
