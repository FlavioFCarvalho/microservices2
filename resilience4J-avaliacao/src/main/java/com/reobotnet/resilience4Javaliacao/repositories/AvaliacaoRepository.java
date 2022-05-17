package com.reobotnet.resilience4Javaliacao.repositories;

import java.util.List;
import java.util.Optional;

import com.reobotnet.resilience4Javaliacao.domain.Avaliacao;

public interface AvaliacaoRepository {
	
	void save(Avaliacao avaliacao);
	Optional<Avaliacao> getOne(Long id);
	List<Avaliacao> getAll();
	
}
