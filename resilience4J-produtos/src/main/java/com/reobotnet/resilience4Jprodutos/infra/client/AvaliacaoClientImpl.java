package com.reobotnet.resilience4Jprodutos.infra.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.reobotnet.resilience4Jprodutos.client.avaliacoes.AvaliacaoClient;
import com.reobotnet.resilience4Jprodutos.client.avaliacoes.AvaliacaoModel;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Component
public class AvaliacaoClientImpl implements AvaliacaoClient {

	private final Logger logger = LoggerFactory.getLogger(AvaliacaoClientImpl.class);
	private final RestTemplate restTemplate;

	private final static String API_URL = UriComponentsBuilder.fromHttpUrl("http://localhost:8090/avaliacaos")
			.queryParam("produtoId", "{produtoId}").encode().toUriString();

	private final Map<Long, List<AvaliacaoModel>> CACHE = new HashMap<>();

	public AvaliacaoClientImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	@CircuitBreaker(name = "avaliacaoCB", fallbackMethod = "buscarTodosPorProdutoNoCache")
	public List<AvaliacaoModel> buscarTodosPorProduto(Long produtoId) {
		final List<AvaliacaoModel> avaliacoes = executarRequisicao(produtoId);
		return avaliacoes;
	}

	private List<AvaliacaoModel> executarRequisicao(Long produtoId) {
		final Map<String, Object> parametros = new HashMap<>();
		parametros.put("produtoId", produtoId);

		logger.info("Buscando avaliações");
		final AvaliacaoModel[] avaliacoes;

		try {
			avaliacoes = restTemplate.getForObject(API_URL, AvaliacaoModel[].class, parametros);
		} catch (Exception e) {
			logger.error("Erro ao buscar avaliações");
			throw e;
		}

		logger.info("Alimentando cache");
		CACHE.put(produtoId, Arrays.asList(avaliacoes));

		return Arrays.asList(avaliacoes);
	}

	private List<AvaliacaoModel> buscarTodosPorProdutoNoCache(Long produtoId, Throwable e) {
		logger.info("Buscando no cache");
		return CACHE.getOrDefault(produtoId, new ArrayList());

	}

}
