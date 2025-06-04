package com.wagnerdf.fancollectorsmedia.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wagnerdf.fancollectorsmedia.model.Papel;
import com.wagnerdf.fancollectorsmedia.repository.PapelRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initPapeis(PapelRepository papelRepository) {
        return args -> {
            if (papelRepository.findByNome("ROLE_USER").isEmpty()) {
                Papel papelUser = new Papel();
                papelUser.setNome("ROLE_USER");
                papelRepository.save(papelUser);
                System.out.println("✅ Papel ROLE_USER criado");
            }

            if (papelRepository.findByNome("ROLE_ADMIN").isEmpty()) {
                Papel papelAdmin = new Papel();
                papelAdmin.setNome("ROLE_ADMIN");
                papelRepository.save(papelAdmin);
                System.out.println("✅ Papel ROLE_ADMIN criado");
            }
        };
    }
}
