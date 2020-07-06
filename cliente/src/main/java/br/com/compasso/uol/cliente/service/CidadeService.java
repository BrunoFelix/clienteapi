package br.com.compasso.uol.cliente.service;

import org.springframework.http.ResponseEntity;

import br.com.compasso.uol.cliente.model.entity.Cidade;
import br.com.compasso.uol.cliente.model.response.Response;

public interface CidadeService {
	
	/**
	 * Inserir registro Cliente
	 * @param Cidade cidade
	 * @return Response
	 */
	public ResponseEntity<Response> add(Cidade cliente);
	
	/**
	 * Pesquisar resgistros de Cidades através do campo nome
	 * @param String nome
	 * @return Response
	 */
	public ResponseEntity<Response> findByNome(String nome);
	
	/**
	 * Pesquisar resgistros de Cidades através do campo estado
	 * @param String estado
	 * @return Response
	 */
	public ResponseEntity<Response> findByEstado(String estado);
}
