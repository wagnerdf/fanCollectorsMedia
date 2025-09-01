-- 1. Renomear "Jogo Físico (Console)" para "Jogos Games"
UPDATE midia_tipo
SET nome = 'Jogos Games'
WHERE nome = 'Jogo Físico (Console)';

-- 2. Ativar o campo ativo para "Jogos Games"
UPDATE midia_tipo
SET ativo = TRUE
WHERE nome = 'Jogos Games';

-- 3. Deletar registros desnecessários
DELETE FROM midia_tipo
WHERE nome IN ('Jogo de PC (CD/DVD)', 'Cartucho de Videogame');
