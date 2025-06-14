package com.wagnerdf.fancollectorsmedia.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wagnerdf.fancollectorsmedia.dto.CadastroHobbyDto;
import com.wagnerdf.fancollectorsmedia.model.Cadastro;
import com.wagnerdf.fancollectorsmedia.model.CadastroHobby;
import com.wagnerdf.fancollectorsmedia.model.Hobby;
import com.wagnerdf.fancollectorsmedia.repository.CadastroHobbyRepository;
import com.wagnerdf.fancollectorsmedia.repository.CadastroRepository;
import com.wagnerdf.fancollectorsmedia.repository.HobbyRepository;

@Service
public class CadastroHobbyService {

	@Autowired
	private CadastroHobbyRepository cadastroHobbyRepository;

	@Autowired
	private HobbyRepository hobbyRepository;

	@Autowired
	private CadastroRepository cadastroRepository;

	public CadastroHobbyService(CadastroHobbyRepository cadastroHobbyRepository, HobbyRepository hobbyRepository) {
		this.cadastroHobbyRepository = cadastroHobbyRepository;
		this.hobbyRepository = hobbyRepository;
	}

	public List<CadastroHobbyDto> listarPorCadastroId(Long cadastroId) {
		List<CadastroHobby> entidades = cadastroHobbyRepository.findByCadastroId(cadastroId);

		return entidades.stream().map(entidade -> {
			CadastroHobbyDto dto = new CadastroHobbyDto();
			dto.setId(entidade.getId());
			dto.setCadastroId(entidade.getCadastro().getId());
			dto.setHobbyId(entidade.getHobby().getId());
			dto.setDataRegistro(entidade.getDataRegistro());
			dto.setNivelInteresse(entidade.getNivelInteresse());
			dto.setNomeHobby(entidade.getHobby().getNome());
			dto.setDescricaoHobby(entidade.getHobby().getDescricao());
			return dto;
		}).toList();
	}

	public CadastroHobbyDto adicionar(CadastroHobbyDto dto) {
		Cadastro cadastro = cadastroRepository.findById(dto.getCadastroId())
				.orElseThrow(() -> new RuntimeException("Cadastro não encontrado"));
		Hobby hobby = hobbyRepository.findById(dto.getHobbyId())
				.orElseThrow(() -> new RuntimeException("Hobby não encontrado"));

		CadastroHobby ch = new CadastroHobby();
		ch.setCadastro(cadastro);
		ch.setHobby(hobby);
		ch.setNivelInteresse(dto.getNivelInteresse());
		ch.setDataRegistro(LocalDateTime.now());

		return toDto(cadastroHobbyRepository.save(ch));
	}

	public CadastroHobbyDto atualizar(Long id, CadastroHobbyDto dto) {
		CadastroHobby ch = cadastroHobbyRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("CadastroHobby não encontrado"));

		ch.setNivelInteresse(dto.getNivelInteresse());
		return toDto(cadastroHobbyRepository.save(ch));
	}

	public void remover(Long id) {
		cadastroHobbyRepository.deleteById(id);
	}

	private CadastroHobbyDto toDto(CadastroHobby ch) {
		CadastroHobbyDto dto = new CadastroHobbyDto();
		dto.setId(ch.getId());
		dto.setCadastroId(ch.getCadastro().getId());
		dto.setHobbyId(ch.getHobby().getId());
		dto.setNivelInteresse(ch.getNivelInteresse());
		dto.setDataRegistro(ch.getDataRegistro());
		dto.setNomeHobby(ch.getHobby().getNome());
		dto.setDescricaoHobby(ch.getHobby().getDescricao());
		return dto;
	}

	public Optional<CadastroHobby> buscarPorId(Long id) {
		return cadastroHobbyRepository.findById(id);
	}
}
