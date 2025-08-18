package com.wagnerdf.fancollectorsmedia.jobs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wagnerdf.fancollectorsmedia.infra.sql.SqlScriptRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MidiaResetScheduler {

    private static final String SCRIPT_PATH = "scripts/reset_midia.sql";

    private final SqlScriptRunner sqlScriptRunner;

    public MidiaResetScheduler(SqlScriptRunner sqlScriptRunner) {
        this.sqlScriptRunner = sqlScriptRunner;
    }

    // ✅ Somente para ambiente local (testes)
    @Scheduled(initialDelayString = "PT10S", fixedDelayString = "PT48H")
    @ConditionalOnProperty(name = "scheduler.midia.reset.mode", havingValue = "local")
    public void executarResetLocal() {
        log.info("Iniciando reset periódico de mídias (modo LOCAL: delay)...");
        sqlScriptRunner.runScriptFromClasspath(SCRIPT_PATH);
        log.info("Reset concluído (modo LOCAL).");
    }

    // ✅ Somente para produção
    @Scheduled(cron = "0 0 3 */2 * *")
    @ConditionalOnProperty(name = "scheduler.midia.reset.mode", havingValue = "prod")
    public void executarResetProd() {
        log.info("Iniciando reset periódico de mídias (modo PROD: cron)...");
        sqlScriptRunner.runScriptFromClasspath(SCRIPT_PATH);
        log.info("Reset concluído (modo PROD).");
    }
}


