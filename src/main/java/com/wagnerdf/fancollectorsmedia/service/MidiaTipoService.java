package com.wagnerdf.fancollectorsmedia.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.dto.MidiaTipoDto;
import com.wagnerdf.fancollectorsmedia.model.MidiaTipo;
import com.wagnerdf.fancollectorsmedia.repository.MidiaTipoRepository;

@Service
public class MidiaTipoService {

	@Autowired
	private MidiaTipoRepository midiaTipoRepository;

	public MidiaTipo salvar(MidiaTipo midiaTipo) {
		return midiaTipoRepository.save(midiaTipo);
	}

	public List<MidiaTipo> listarTodos() {
		return midiaTipoRepository.findAll();
	}

	public boolean existePorNome(String nome) {
		return midiaTipoRepository.existsByNome(nome);
	}
	
	public MidiaTipo buscarPorId(Long id) {
	    return midiaTipoRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("MidiaTipo não encontrado com ID: " + id));
	}
	
	public void deletarPorId(Long id) {
		midiaTipoRepository.deleteById(id);
	}
	
	public MidiaTipoService(MidiaTipoRepository midiaTipoRepository) {
        this.midiaTipoRepository = midiaTipoRepository;
    }
	
	public List<MidiaTipoDto> buscarAtivos() {
	    return midiaTipoRepository.findByAtivoTrue().stream()
	        .map(m -> new MidiaTipoDto(m.getId(), m.getNome(), m.getDescricao()))
	        .collect(Collectors.toList());
	}

}
