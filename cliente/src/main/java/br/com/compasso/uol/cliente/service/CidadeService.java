package br.com.compasso.uol.cliente.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.compasso.uol.cliente.model.entity.Cidade;
import br.com.compasso.uol.cliente.model.response.Response;

public interface CidadeService {
	
	public ResponseEntity<Response> add(Cidade cliente);
		
	public ResponseEntity<Response> findByNome(String nome);
	
	public ResponseEntity<Response> findByEstado(String estado);
}
