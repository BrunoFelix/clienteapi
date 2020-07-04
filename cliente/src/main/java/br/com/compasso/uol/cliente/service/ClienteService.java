package br.com.compasso.uol.cliente.service;

import org.springframework.http.ResponseEntity;

import br.com.compasso.uol.cliente.model.entity.Cliente;
import br.com.compasso.uol.cliente.model.response.Response;

public interface ClienteService {

	public ResponseEntity<Response> add(Cliente cliente);
	
	public ResponseEntity<Response> update(Cliente cliente);
	
	public ResponseEntity<Response> delete(Long id);
	
	public ResponseEntity<Response> findById(Long id);
	
	public ResponseEntity<Response> findByNome(String nome);
	
	
}
