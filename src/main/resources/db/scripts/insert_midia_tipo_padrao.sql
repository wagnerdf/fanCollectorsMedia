-- Limpa as tabelas midia e midia_tipo e reinicia os IDs
TRUNCATE TABLE midia, midia_tipo RESTART IDENTITY;

-- Insere tipos de mídia colecionáveis
INSERT INTO midia_tipo (nome, descricao) VALUES
('DVD', 'Disco digital usado para filmes, shows e conteúdos multimídia.'),
('Blu-ray', 'Disco de alta definição usado para filmes e jogos.'),
('CD', 'Disco compacto usado para músicas, softwares ou dados.'),
('Vinil', 'Disco de vinil usado para músicas com qualidade analógica.'),
('K7 (Fita Cassete)', 'Mídia analógica magnética para gravação de áudio.'),
('VHS', 'Fita de vídeo analógica usada para gravação e reprodução de filmes.'),
('LaserDisc', 'Disco óptico usado antes do DVD, geralmente em formato grande.'),
('Digital (Arquivos)', 'Coleções digitais como MP3, FLAC, vídeos ou livros digitais.'),
('Jogo Físico (Console)', 'Mídias físicas como cartuchos ou discos para consoles de videogame.'),
('Jogo de PC (CD/DVD)', 'Jogos de computador distribuídos em CD, DVD ou mídia física.'),
('Cartucho de Videogame', 'Cartuchos de jogos clássicos (NES, SNES, Mega Drive etc).'),
('Livro Físico', 'Livros impressos colecionáveis (literatura, mangás, quadrinhos).'),
('Revista/Quadrinhos', 'Coleções de revistas, HQs, mangás ou fanzines.'),
('Cédula', 'Dinheiro em papel de diferentes países e épocas.'),
('Moeda', 'Moedas metálicas antigas ou comemorativas.'),
('Cartões (Trading Cards)', 'Cartões colecionáveis (Pokémon, Magic, Yu-Gi-Oh!, etc).'),
('Miniaturas/Estatuetas', 'Peças decorativas de personagens, séries ou temas específicos.'),
('Pôster', 'Pôsteres decorativos ou promocionais de filmes, bandas, etc.'),
('Figurinha (Álbum)', 'Figurinhas colecionáveis de álbuns temáticos (futebol, séries etc).'),
('Colecionável Digital (NFT)', 'Itens digitais únicos baseados em blockchain.');
