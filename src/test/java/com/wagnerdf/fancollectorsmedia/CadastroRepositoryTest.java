package com.wagnerdf.fancollectorsmedia;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ActiveProfiles;

import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.enums.StatusUsuario;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;

@DataJpaTest
@ActiveProfiles("test")
public class CadastroRepositoryTest {

    @Autowired
    private CadastroRepository cadastroRepository;

    @Test
    public void deveSalvarCadastroComSucesso() {
        Cadastro cadastro = new Cadastro();
        cadastro.setNome("Wagner");
        cadastro.setSobreNome("Silva");
        cadastro.setEmail("wagner@email.com");
        cadastro.setDataNascimento(LocalDate.of(1990, 5, 20));
        cadastro.setStatus(StatusUsuario.ATIVO);
        cadastro.setDataCadastro(LocalDateTime.now());

        Cadastro salvo = cadastroRepository.save(cadastro);

        assertThat(salvo).isNotNull();
        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getEmail()).isEqualTo("wagner@email.com");
    }
}
