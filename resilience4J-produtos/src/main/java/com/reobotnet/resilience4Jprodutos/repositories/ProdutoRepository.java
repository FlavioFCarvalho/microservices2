package com.reobotnet.resilience4Jprodutos.repositories;

import java.util.List;
import java.util.Optional;

import com.reobotnet.resilience4Jprodutos.domain.Produto;

public interface ProdutoRepository {
	
	void save(Produto produto);
	Optional<Produto> getOne(Long id);
	List<Produto> getAll();
	
}