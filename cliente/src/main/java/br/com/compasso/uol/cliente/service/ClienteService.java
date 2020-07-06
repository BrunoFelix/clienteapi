package br.com.compasso.uol.cliente.service;

import org.springframework.http.ResponseEntity;

import br.com.compasso.uol.cliente.model.entity.Cliente;
import br.com.compasso.uol.cliente.model.response.Response;

public interface ClienteService {

	/**
	 * Inserir registro Cliente
	 * @param Cliente cliente
	 * @return Response
	 */
	public ResponseEntity<Response> add(Cliente cliente);
	
	/**
	 * Atualizar registro Cliente
	 * @param Cliente cliente
	 * @return Response
	 */
	public ResponseEntity<Response> update(Cliente cliente);
	
	/**
	 * Deletar registro Cliente atráves do id
	 * @param Long id
	 * @return Response
	 */
	public ResponseEntity<Response> delete(Long id);
	
	/**
	 * Pesquisar resgistros de Clientes através do campo id
	 * @param Long id
	 * @return Response
	 */
	public ResponseEntity<Response> findById(Long id);
	
	/**
	 * Pesquisar resgistros de Clientes através do campo nome
	 * @param String nome
	 * @return Response
	 */
	public ResponseEntity<Response> findByNome(String nome);
	
}
