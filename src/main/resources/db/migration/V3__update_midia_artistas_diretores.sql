ALTER TABLE midia
RENAME COLUMN artista_diretor TO artistas;

ALTER TABLE midia
ADD COLUMN diretores VARCHAR(255);
