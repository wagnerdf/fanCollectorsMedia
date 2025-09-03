package com.wagnerdf.fancollectorsmedia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wagnerdf.fancollectorsmedia.service.ApiConfigService;

import jakarta.annotation.PostConstruct;

@Component
public class IgdbConfigInitializer {

    @Autowired
    private ApiConfigService apiConfigService;

    private String clientId;
    private String clientSecret;

    @PostConstruct
    public void init() {
        this.clientId = apiConfigService.obterValor("IGDB_CLIENT_ID");
        this.clientSecret = apiConfigService.obterValor("IGDB_CLIENT_SECRET");

        System.out.println("ClientId carregado do banco: " + clientId);
        System.out.println("ClientSecret carregado do banco: " + clientSecret);
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}

