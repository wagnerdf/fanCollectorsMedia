package com.wagnerdf.fancollectorsmedia.infra.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SqlScriptRunner {

    private final DataSource dataSource;

    public SqlScriptRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Executa um script SQL a partir do classpath, forçando UTF-8.
     * Funciona tanto em ambiente local quanto em produção (Railway).
     * 
     * @param classpathLocation caminho do script dentro do resources
     */
    public void runScriptFromClasspath(String classpathLocation) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            Resource resource = new ClassPathResource(classpathLocation);

            log.info("Iniciando execução do script SQL (UTF-8): {}", classpathLocation);

            // Configura ResourceDatabasePopulator para UTF-8
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.setSqlScriptEncoding("UTF-8"); // ✅ garante UTF-8
            populator.addScript(resource);

            // Executa o script
            populator.execute(dataSource);

            conn.commit();
            log.info("Script SQL executado com sucesso: {}", classpathLocation);
        } catch (Exception e) {
            log.error("Erro ao executar script SQL: {}", classpathLocation, e);
            if (conn != null) {
                try {
                    conn.rollback();
                    log.info("Rollback realizado com sucesso para o script: {}", classpathLocation);
                } catch (SQLException ex) {
                    log.error("Falha ao realizar rollback do script: {}", classpathLocation, ex);
                }
            }
            throw new RuntimeException("Erro executando script SQL: " + classpathLocation, e);
        } finally {
            if (conn != null) {
                try { 
                    conn.close(); 
                } catch (SQLException ignored) {}
            }
        }
    }
}
