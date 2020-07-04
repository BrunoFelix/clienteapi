package br.com.compasso.uol.cliente.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.compasso.uol.cliente.model.entity.Cliente;
import br.com.compasso.uol.cliente.model.response.Response;
import br.com.compasso.uol.cliente.repository.ClienteRepository;
import br.com.compasso.uol.cliente.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService{
	
	Response response = new Response();
	List<String> listaErro = new ArrayList<String>();
	HttpStatus httpStatus = null;
	String mensagem = null;
	Object data = null;

	@Autowired
	private ClienteRepository clienteRep;
	
	@Override
	public ResponseEntity<Response> add(Cliente cliente) {
		// TODO Auto-generated method stub
		limparVariaveis();
		try {
			//validacoes
			List<Cliente> listaCliente = new ArrayList<Cliente>();
			clienteRep.findByNome(cliente.getNome()).forEach(listaCliente::add);
			
			if (listaCliente.size() > 0) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Já existe um cliente com o nome informado!";
				this.data = null;
			} else {
				Cliente retornoCliente = clienteRep.save(cliente);
				
				if (retornoCliente != null) {
					this.httpStatus = HttpStatus.CREATED;
					this.mensagem = "Registro criado com sucesso!";
					this.data = retornoCliente;
				}
			}
		} catch (Exception e) {
			preencherException(e.getMessage());
		}
		
		this.response.setHttpStatus(httpStatus);
		this.response.setErros(listaErro);
		this.response.setMensagem(mensagem);
		this.response.setData(data);
		return new ResponseEntity<>(this.response, this.response.getHttpStatus());
	}

	@Override
	public ResponseEntity<Response> update(Cliente cliente) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Response> delete(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Response> findById(Long id) {
		// TODO Auto-generated method stub
		this.response = new Response();
		this.listaErro = new ArrayList<String>();
		this.httpStatus = null;
		this.mensagem = null;
		this.data = null;
		try {
			if (id == null || id <= 0) {
				this.httpStatus = HttpStatus.NOT_FOUND;
				this.mensagem = "Digite um ID válido!";
				this.listaErro.add("ID informado inválido!");
			}else {	
				Optional<Cliente> cliente = clienteRep.findById(id);
				
				if (!cliente.isPresent()) {
					this.httpStatus = HttpStatus.NO_CONTENT;
				}else {
					this.httpStatus = HttpStatus.OK;
					this.data = cliente.get();
				}
			}
		}catch (Exception e) {
			preencherException(e.getMessage());
		}
		
		this.response.setHttpStatus(httpStatus);
		this.response.setErros(listaErro);
		this.response.setMensagem(mensagem);
		this.response.setData(data);
		return new ResponseEntity<>(this.response, this.response.getHttpStatus());
	}

	@Override
	public ResponseEntity<Response> findByNome(String nome) {
		// TODO Auto-generated method stub
		limparVariaveis();
		
		try {
			if (nome == null || nome.trim().isEmpty()) {
				this.httpStatus = HttpStatus.NOT_FOUND;
				this.mensagem = "Digite um nome válido!";
				this.listaErro.add("Nome informado inválido!");
			}else {	
				List<Cliente> listaCliente = new ArrayList<Cliente>();
				clienteRep.findByNome(nome).forEach(listaCliente::add);
				
				if (listaCliente.size() <= 0) {
					this.httpStatus = HttpStatus.NO_CONTENT;
				}else {
					this.httpStatus = HttpStatus.OK;
					this.data = listaCliente;
					this.mensagem = listaCliente.size() + (listaCliente.size() == 1 ? " Registro" : "Registros");
				}
			}
		}catch (Exception e) {
			preencherException(e.getMessage());
		}
		
		this.response.setHttpStatus(httpStatus);
		this.response.setErros(listaErro);
		this.response.setMensagem(mensagem);
		this.response.setData(data);
		return new ResponseEntity<>(this.response, this.response.getHttpStatus());
	}
	
	private void limparVariaveis() {
		this.response = new Response();
		this.listaErro = new ArrayList<String>();
		this.httpStatus = null;
		this.mensagem = null;
		this.data = null;
	}
	
	public void preencherException(String erro) {
		this.listaErro.clear();
		this.listaErro.add(erro);
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		this.data = null;
		mensagem = "Servidor não conseguiu processar a solicitação!";
	}

}
