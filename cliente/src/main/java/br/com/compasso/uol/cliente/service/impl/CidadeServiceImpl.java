package br.com.compasso.uol.cliente.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.compasso.uol.cliente.model.entity.Cidade;
import br.com.compasso.uol.cliente.model.response.Response;
import br.com.compasso.uol.cliente.service.CidadeService;

@Service
public class CidadeServiceImpl implements CidadeService {

	@Override
	public ResponseEntity<Response> add(Cidade cliente) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Response> findByNome(String nome) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Response> findByEstado(String estado) {
		// TODO Auto-generated method stub
		return null;
	}

}
