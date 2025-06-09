package com.wagnerdf.fancollectorsmedia.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagnerdf.fancollectorsmedia.dto.MidiaTipoDto;
import com.wagnerdf.fancollectorsmedia.model.MidiaTipo;
import com.wagnerdf.fancollectorsmedia.service.MidiaTipoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/midia-tipos")
@Validated
public class MidiaTipoController {

	@Autowired
	private MidiaTipoService midiaTipoService;

	@PostMapping
	public ResponseEntity<?> criarTipoMidia(@Valid @RequestBody MidiaTipoDto dto) {
		if (midiaTipoService.existePorNome(dto.getNome())) {
	        return ResponseEntity
	                .status(400)
	                .body(Map.of("erro", "Tipo de mídia com esse nome já está cadastrado."));
	    }

		MidiaTipo tipo = new MidiaTipo();
		tipo.setNome(dto.getNome());
		tipo.setDescricao(dto.getDescricao());

		MidiaTipo salvo = midiaTipoService.salvar(tipo);
		return ResponseEntity.status(201).body(salvo);
	}
	
	@GetMapping
    public ResponseEntity<List<MidiaTipo>> listarTipos() {
        return ResponseEntity.ok(midiaTipoService.listarTodos());
    }
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarTipoMidia(@PathVariable Long id){
		Optional<MidiaTipo> tipoExistente = midiaTipoService.buscarPorId(id);
		
		if (tipoExistente.isEmpty()) {
			return ResponseEntity.status(404).body(Map.of("erro", "Tipo de midia não encontrado."));
		}
		
		midiaTipoService.deletarPorId(id);
		return ResponseEntity.ok(Map.of("mensagem", "Tipo de midia deletado com sucesso."));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarTipoMidia(@PathVariable Long id, @Valid @RequestBody MidiaTipoDto dto) {
		Optional<MidiaTipo> existente = midiaTipoService.buscarPorId(id);
		
		if(existente.isEmpty()) {
			return ResponseEntity.status(404).body(Map.of("erro", "Tipo de mídia não encontrado."));
		}
		
		// Verifica se já existe outro com o mesmo nome
	    boolean nomeJaExiste = midiaTipoService.existePorNome(dto.getNome()) && !existente.get().getNome().equalsIgnoreCase(dto.getNome());
	    
	    if(nomeJaExiste) {
	    	return ResponseEntity.status(400).body(Map.of("erro", "Já existe outro tipo de mídia com esse nome."));
	    }
	    
	    MidiaTipo tipo = existente.get();
	    tipo.setNome(dto.getNome());
	    tipo.setDescricao(dto.getDescricao());

	    MidiaTipo atualizado = midiaTipoService.salvar(tipo);
	    return ResponseEntity.ok(atualizado);
	}

}




