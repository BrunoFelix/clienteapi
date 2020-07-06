package br.com.compasso.uol.cliente.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.compasso.uol.cliente.model.entity.Cidade;
import br.com.compasso.uol.cliente.model.response.Response;
import br.com.compasso.uol.cliente.service.CidadeService;

@RestController
@RequestMapping(value="/api/cidade")
public class CidadeController {

	@Autowired
	private CidadeService cidadeService;
	
	@PostMapping("/add")
	public ResponseEntity<Response> add(@Valid @RequestBody Cidade cidade){
		return cidadeService.add(cidade);
	}

	@GetMapping(value = "/estado/{estado}")
	public ResponseEntity<Response> findByEstado(@PathVariable("estado") String estado){
		return cidadeService.findByEstado(estado);
	}
	
	@GetMapping(value = "/nome/{nome}")
	public ResponseEntity<Response> findByNome(@PathVariable("nome") String nome){
		return cidadeService.findByNome(nome);
	}
}
