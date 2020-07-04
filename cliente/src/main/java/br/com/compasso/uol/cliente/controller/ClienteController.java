package br.com.compasso.uol.cliente.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.compasso.uol.cliente.model.entity.Cliente;
import br.com.compasso.uol.cliente.model.response.Response;
import br.com.compasso.uol.cliente.service.ClienteService;

@RestController
@RequestMapping(value="/cliente")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	
	@PostMapping("/add")
	public ResponseEntity<Response> add(@Valid @RequestBody Cliente cliente){
		return clienteService.add(cliente);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Response> update(@RequestBody Cliente cliente){
		return clienteService.update(cliente);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Response> add(@PathVariable("id") Long id){
		return clienteService.delete(id);
	}
	
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<Response> findById(@PathVariable("id") Long id){
		return clienteService.findById(id);
	}
	
	@GetMapping(value = "/nome/{nome}")
	public ResponseEntity<Response> findByNome(@PathVariable("nome") String nome){
		return clienteService.findByNome(nome);
	}
}
