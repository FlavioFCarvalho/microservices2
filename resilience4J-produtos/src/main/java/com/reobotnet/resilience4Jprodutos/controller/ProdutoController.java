package com.reobotnet.resilience4Jprodutos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reobotnet.resilience4Jprodutos.client.avaliacoes.AvaliacaoClient;
import com.reobotnet.resilience4Jprodutos.client.avaliacoes.AvaliacaoModel;
import com.reobotnet.resilience4Jprodutos.domain.Produto;
import com.reobotnet.resilience4Jprodutos.exceptions.RecursoNaoEncontradoException;
import com.reobotnet.resilience4Jprodutos.model.ProdutoModel;
import com.reobotnet.resilience4Jprodutos.repositories.ProdutoRepository;



@RestController
@RequestMapping("/produtos")
public class ProdutoController {
	
	private final ProdutoRepository produtos;
	private final AvaliacaoClient avaliacoes;

	public ProdutoController(ProdutoRepository produtos, AvaliacaoClient avaliacoes) {
		this.produtos = produtos;
		this.avaliacoes = avaliacoes;
	}

	@GetMapping
	public List<ProdutoModel> buscarTodos() {
		return produtos.getAll()
				.stream()
				.map(this::converterProdutoParaModelo)
				.collect(Collectors.toList());
	}

	@GetMapping("/{produtoId}")
	public ProdutoModel buscarPorId(@PathVariable Long produtoId) {
		return produtos.getOne(produtoId)
				.map(this::converterProdutoParaModeloComAvaliacao)
				.orElseThrow(RecursoNaoEncontradoException::new);
	}
	
	private ProdutoModel converterProdutoParaModelo(Produto produto) {
		return ProdutoModel.of(produto);
	}
	
	private ProdutoModel converterProdutoParaModeloComAvaliacao(Produto produto) {
		return ProdutoModel.of(produto, buscarAvaliacaoDoProduto(produto.getId()));
	}

	private List<AvaliacaoModel> buscarAvaliacaoDoProduto(Long productId) {
		return avaliacoes.buscarTodosPorProduto(productId);
	}
}
