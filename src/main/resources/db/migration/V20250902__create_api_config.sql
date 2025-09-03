CREATE TABLE api_config (
    id SERIAL PRIMARY KEY,
    chave VARCHAR(100) NOT NULL UNIQUE,
    valor TEXT NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/* Criação das chaves principais para a API de games */

INSERT INTO api_config (chave, valor) VALUES ('IGDB_CLIENT_ID', '');
INSERT INTO api_config (chave, valor) VALUES ('IGDB_ACCESS_TOKEN', '');
INSERT INTO api_config (chave, valor) VALUES ('IGDB_REFRESH_TOKEN', '');