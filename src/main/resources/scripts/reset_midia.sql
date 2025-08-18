-- Deleta todas as mídias do usuário de teste (id = 43)
DELETE FROM midia
WHERE cadastro_id = 43;

-- Reinseri a mídia de teste

INSERT INTO midia (
    titulo_original,
    titulo_alternativo, 
    ano_lancamento, 
    artistas, 
    cadastro_id, 
    midia_tipo_id
) VALUES (
    'The Avengers',
    'Os Vingadores: The Avengers', 
    2012, 
    'TRobert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth, Scarlett Johansson', 
    43, 
    2
);
