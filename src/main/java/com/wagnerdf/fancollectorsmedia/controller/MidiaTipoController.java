package com.wagnerdf.fancollectorsmedia.controller;

import java.util.List;
import java.util.Map;

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
			return ResponseEntity.status(400).body(Map.of("erro", "Tipo de mídia com esse nome já está cadastrado."));
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
	public ResponseEntity<?> deletarTipoMidia(@PathVariable Long id) {
		try {
			midiaTipoService.buscarPorId(id); // Só para checar existência
		} catch (RuntimeException e) {
			return ResponseEntity.status(404).body(Map.of("erro", "Tipo de mídia não encontrado."));
		}

		midiaTipoService.deletarPorId(id);
		return ResponseEntity.ok(Map.of("mensagem", "Tipo de mídia deletado com sucesso."));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarTipoMidia(@PathVariable Long id, @Valid @RequestBody MidiaTipoDto dto) {
		MidiaTipo existente;
		try {
			existente = midiaTipoService.buscarPorId(id);
		} catch (RuntimeException e) {
			return ResponseEntity.status(404).body(Map.of("erro", "Tipo de mídia não encontrado."));
		}

		// Verifica se já existe outro com o mesmo nome (e que não seja esse)
		boolean nomeJaExiste = midiaTipoService.existePorNome(dto.getNome())
				&& !existente.getNome().equalsIgnoreCase(dto.getNome());

		if (nomeJaExiste) {
			return ResponseEntity.status(400).body(Map.of("erro", "Já existe outro tipo de mídia com esse nome."));
		}

		existente.setNome(dto.getNome());
		existente.setDescricao(dto.getDescricao());

		MidiaTipo atualizado = midiaTipoService.salvar(existente);
		return ResponseEntity.ok(atualizado);
	}
	
	@GetMapping("/ativos")
    public List<MidiaTipoDto> listarAtivos() {
        return midiaTipoService.buscarAtivos();
    }

}
