package com.wagnerdf.fancollectorsmedia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wagnerdf.fancollectorsmedia.model.Hobby;
import com.wagnerdf.fancollectorsmedia.service.HobbyService;

@RestController
@RequestMapping("/api/hobbies")
public class HobbyController {

    @Autowired
    private HobbyService hobbyService;

    @GetMapping
    public List<Hobby> listarTodos() {
        return hobbyService.listarTodos();
    }
}
