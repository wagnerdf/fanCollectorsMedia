package com.wagnerdf.fancollectorsmedia.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.fancollectorsmedia.service.ApiConfigService;

@RestController
@RequestMapping("/api/config")
public class ApiConfigController {

    @Autowired
    private ApiConfigService service;

    // Buscar valor de uma chave específica
    @GetMapping("/{chave}")
    public ResponseEntity<String> getConfig(@PathVariable String chave) {
        return ResponseEntity.ok(service.getValor(chave));
    }

    // Atualizar valor de uma chave
    @PutMapping("/{chave}")
    public ResponseEntity<Void> updateConfig(@PathVariable String chave, @RequestBody Map<String, String> body) {
        String novoValor = body.get("valor");
        service.atualizarValor(chave, novoValor);
        return ResponseEntity.noContent().build();
    }

    // Opcional: listar todas as configs (sem valores sensíveis)
    @GetMapping
    public ResponseEntity<?> listarConfigs() {
        return ResponseEntity.ok("⚠️ Por segurança, apenas admin deve ver todas as configs!");
    }
}
