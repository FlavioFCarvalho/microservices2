package com.reobotnet.resilience4Javaliacao.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reobotnet.resilience4Javaliacao.model.AvaliacaoModel;
import com.reobotnet.resilience4Javaliacao.repositories.AvaliacaoRepository;

@RestController
@RequestMapping("/avaliacaos")
public class AvaliacaoController {
	
	private final AvaliacaoRepository avaliacaos;

	public AvaliacaoController(AvaliacaoRepository avaliacaos) {
		this.avaliacaos = avaliacaos;
	}

	@GetMapping
	public List<AvaliacaoModel> buscarPorProduto(@RequestParam Long produtoId) {
		return avaliacaos.getAll()
				.stream()
				.filter(avaliacao -> avaliacao.getProdutoId().equals(produtoId))
				.map(AvaliacaoModel::of)
				.collect(Collectors.toList());
	}
}
