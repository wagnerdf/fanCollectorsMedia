package com.wagnerdf.fancollectorsmedia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.model.Hobby;
import com.wagnerdf.fancollectorsmedia.repository.HobbyRepository;

@Service
public class HobbyService {

    @Autowired
    private HobbyRepository hobbyRepository;

    public List<Hobby> listarTodos() {
        return hobbyRepository.findAll();
    }
}
