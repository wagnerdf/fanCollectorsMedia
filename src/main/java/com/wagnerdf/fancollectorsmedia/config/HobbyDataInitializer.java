package com.wagnerdf.fancollectorsmedia.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wagnerdf.fancollectorsmedia.model.Hobby;
import com.wagnerdf.fancollectorsmedia.repository.HobbyRepository;

@Configuration
public class HobbyDataInitializer {

    @Bean
    public CommandLineRunner loadHobbies(HobbyRepository hobbyRepository) {
        return args -> {
            if (hobbyRepository.count() == 0) {
                List<Hobby> hobbies = Arrays.asList(
                    createHobby("Leitura", "Apreciar livros, revistas ou quadrinhos para entretenimento ou aprendizado."),
                    createHobby("Filmes e Séries", "Assistir produções audiovisuais em plataformas de streaming ou cinema."),
                    createHobby("Videogames", "Jogar jogos digitais em consoles, computadores ou dispositivos móveis."),
                    createHobby("Colecionismo", "Reunir e organizar itens como moedas, selos, brinquedos, etc."),
                    createHobby("Esportes", "Praticar ou acompanhar esportes como futebol, corrida, ciclismo, etc."),
                    createHobby("Música", "Ouvir, tocar instrumentos ou compor músicas."),
                    createHobby("Tecnologia", "Interesse por programação, eletrônica, gadgets, IA e novidades tecnológicas."),
                    createHobby("Animes e Mangás", "Assistir animes japoneses e/ou ler mangás."),
                    createHobby("Modelismo", "Montar e colecionar modelos em miniatura de carros, aviões, navios, etc."),
                    createHobby("Cosplay", "Se vestir como personagens de filmes, jogos, quadrinhos ou animes."),
                    createHobby("Culinária", "Cozinhar por prazer e experimentar novas receitas."),
                    createHobby("Desenho e Pintura", "Criar arte visual utilizando diferentes técnicas e materiais."),
                    createHobby("Fotografia", "Capturar momentos e paisagens com câmeras ou celulares."),
                    createHobby("Cinema", "Apreciar obras cinematográficas clássicas ou contemporâneas."),
                    createHobby("Astronomia", "Observar estrelas, planetas e fenômenos celestes."),
                    createHobby("História", "Estudar eventos históricos, civilizações e personagens do passado."),
                    createHobby("Puzzles e Quebra-cabeças", "Montar ou resolver desafios lógicos."),
                    createHobby("Artesanato", "Criar objetos manuais como crochê, bordado, origami, etc."),
                    createHobby("Caminhadas e Trilhas", "Explorar a natureza a pé como forma de lazer e exercício."),
                    createHobby("Escrita", "Escrever contos, poesias, blogs ou diários."),
                    createHobby("DIY (Faça Você Mesmo)", "Projetos caseiros de decoração, reparos ou invenções."),
                    createHobby("Jardinagem", "Cultivar plantas, flores e hortas."),
                    createHobby("Meditação e Yoga", "Práticas para relaxamento e equilíbrio mental e corporal."),
                    createHobby("Voluntariado", "Ajudar causas sociais ou comunitárias."),
                    createHobby("Mágica/Ilusionismo", "Praticar truques e técnicas de mágica."),
                    createHobby("Stand-up Comedy", "Apreciar ou realizar apresentações humorísticas."),
                    createHobby("Carros e Motos", "Interesse por veículos, modificações e mecânica."),
                    createHobby("Idiomas", "Estudar e praticar novas línguas."),
                    createHobby("RPG de Mesa", "Participar de jogos de interpretação de papéis com regras."),
                    createHobby("Board Games", "Jogar jogos de tabuleiro com amigos ou família.")
                );

                hobbyRepository.saveAll(hobbies);
                System.out.println("Hobbies padrões inseridos com sucesso.");
            }
        };
    }

    private Hobby createHobby(String nome, String descricao) {
        Hobby hobby = new Hobby(nome);
        hobby.setDescricao(descricao);
        return hobby;
    }
}
