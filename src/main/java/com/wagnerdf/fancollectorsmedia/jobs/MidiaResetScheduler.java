package com.wagnerdf.fancollectorsmedia.jobs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wagnerdf.fancollectorsmedia.infra.sql.SqlScriptRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(name = "seed.reset.enabled", havingValue = "true", matchIfMissing = true)
public class MidiaResetScheduler {

    private static final String SCRIPT_PATH = "scripts/reset_midia.sql";

    private final SqlScriptRunner sqlScriptRunner;

    public MidiaResetScheduler(SqlScriptRunner sqlScriptRunner) {
        this.sqlScriptRunner = sqlScriptRunner;
    }

    // Roda 10s após subir a app (para facilitar testes) e depois a cada 48h, contando do fim da execução
    @Scheduled(initialDelayString = "PT10S", fixedDelayString = "PT48H", cron = "0 0 3 */2 * *")
    public void executarResetPeriodico() {
        log.info("Iniciando reset periódico de mídias do usuário de teste (id=43)...");
        sqlScriptRunner.runScriptFromClasspath(SCRIPT_PATH);
        log.info("Reset periódico concluído.");
    }

    
}

