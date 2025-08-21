-- Adiciona a coluna midia_tipo_nome na tabela de mídias
ALTER TABLE midia
ADD COLUMN midia_tipo_nome VARCHAR(100);

-- Popula os valores atuais a partir da tabela de tipos de mídia
UPDATE midia m
SET midia_tipo_nome = tm.nome
FROM midia_tipo tm
WHERE m.midia_tipo_id = tm.id;

-- Opcional: criar índice para melhorar buscas futuras
CREATE INDEX idx_midia_tipo_nome ON midia(midia_tipo_nome);
