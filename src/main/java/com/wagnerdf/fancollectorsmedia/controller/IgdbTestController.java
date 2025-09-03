package com.wagnerdf.fancollectorsmedia.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.fancollectorsmedia.service.IgdbService;

@RestController
@RequestMapping("/api/igdb")
public class IgdbTestController {

    @Autowired
    private IgdbService igdbService;

    @GetMapping("/token")
    public Map<String, String> getToken() {
        String token = igdbService.getAccessToken();
        return Map.of("access_token", token);
    }
}
