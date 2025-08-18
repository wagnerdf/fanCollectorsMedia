package com.wagnerdf.fancollectorsmedia.infra.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SqlScriptRunner {

    private final DataSource dataSource;

    public SqlScriptRunner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void runScriptFromClasspath(String classpathLocation) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            log.info("Executando script SQL: {}", classpathLocation);
            ScriptUtils.executeSqlScript(conn, new ClassPathResource(classpathLocation));

            conn.commit();
            log.info("Script concluído com sucesso: {}", classpathLocation);
        } catch (Exception e) {
            log.error("Falha ao executar script: {}", classpathLocation, e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    log.error("Falha ao fazer rollback do script: {}", classpathLocation, ex);
                }
            }
            throw new RuntimeException("Erro executando script SQL: " + classpathLocation, e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }
}
