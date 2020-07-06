package br.com.compasso.uol.cliente.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.compasso.uol.cliente.model.entity.Cidade;
import br.com.compasso.uol.cliente.model.entity.Cliente;
import br.com.compasso.uol.cliente.model.response.ErrorObject;
import br.com.compasso.uol.cliente.model.response.Response;
import br.com.compasso.uol.cliente.repository.CidadeRepository;
import br.com.compasso.uol.cliente.service.CidadeService;

@Service
public class CidadeServiceImpl implements CidadeService {

	Response response = new Response();
	List<ErrorObject> listaErro = new ArrayList<ErrorObject>();
	HttpStatus httpStatus = null;
	String mensagem = null;
	Object data = null;
	
	@Autowired
	private CidadeRepository cidadeRep;
	
	@Override
	public ResponseEntity<Response> add(Cidade cidade) {
		// TODO Auto-generated method stub
		limparVariaveis();
		try {
			//validacoes
			List<Cidade> listaCidade = new ArrayList<Cidade>();
			cidadeRep.findByNomeAndEstado(cidade.getNome(), cidade.getEstado()).forEach(listaCidade::add);
			
			if (listaCidade.size() > 0) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível inserir o registro!";
				this.listaErro.add(new ErrorObject("Já existe uma cidade com os dados informados!", null, null));
				this.data = null;
			} else {
				Cidade retornoCidade = cidadeRep.save(cidade);
				
				if (retornoCidade != null) {
					this.httpStatus = HttpStatus.CREATED;
					this.mensagem = "Registro criado com sucesso!";
					this.data = retornoCidade;
				}
			}
		} catch (Exception e) {
			preencherException(e.getMessage());
		}
		
		this.response.setHttpStatus(httpStatus);
		this.response.setErrors(listaErro);
		this.response.setMessage(mensagem);
		this.response.setData(data);
		return new ResponseEntity<>(this.response, this.response.getHttpStatus());
	}

	@Override
	public ResponseEntity<Response> findByNome(String nome) {
		// TODO Auto-generated method stub
		limparVariaveis();
		
		try {
			if (nome == null || nome.trim().isEmpty()) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível pesquisar o registro!";
				this.listaErro.add(new ErrorObject("O nome informado é inválido!", null, null));
			}else {	
				List<Cidade> listaCidade = new ArrayList<Cidade>();
				cidadeRep.findByNome(nome).forEach(listaCidade::add);
				
				if (listaCidade.size() <= 0) {
					this.httpStatus = HttpStatus.BAD_REQUEST;
					this.mensagem = "Não foi possível pesquisar o registro!";
					this.listaErro.add(new ErrorObject("Não existe registros no banco de dados com o parâmetro informado!", null, null));
				}else {
					this.httpStatus = HttpStatus.OK;
					this.data = listaCidade;
					this.mensagem = listaCidade.size() + (listaCidade.size() == 1 ? " Registro" : "Registros");
				}
			}
		}catch (Exception e) {
			preencherException(e.getMessage());
		}
		
		this.response.setHttpStatus(httpStatus);
		this.response.setErrors(listaErro);
		this.response.setMessage(mensagem);
		this.response.setData(data);
		return new ResponseEntity<>(this.response, this.response.getHttpStatus());
	}

	@Override
	public ResponseEntity<Response> findByEstado(String estado) {
		// TODO Auto-generated method stub
		limparVariaveis();
		
		try {
			if (estado == null || estado.trim().isEmpty()) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível pesquisar o registro!";
				this.listaErro.add(new ErrorObject("O estado informado é inválido!", null, null));
			}else {	
				List<Cidade> listaCidade = new ArrayList<Cidade>();
				cidadeRep.findByEstado(estado).forEach(listaCidade::add);
				
				if (listaCidade.size() <= 0) {
					this.httpStatus = HttpStatus.BAD_REQUEST;
					this.mensagem = "Não foi possível pesquisar o registro!";
					this.listaErro.add(new ErrorObject("Não existe registros no banco de dados com o parâmetro informado!", null, null));
				}else {
					this.httpStatus = HttpStatus.OK;
					this.data = listaCidade;
					this.mensagem = listaCidade.size() + (listaCidade.size() == 1 ? " Registro" : "Registros");
				}
			}
		}catch (Exception e) {
			preencherException(e.getMessage());
		}
		
		this.response.setHttpStatus(httpStatus);
		this.response.setErrors(listaErro);
		this.response.setMessage(mensagem);
		this.response.setData(data);
		return new ResponseEntity<>(this.response, this.response.getHttpStatus());
	}
	
	private void limparVariaveis() {
		this.response = new Response();
		this.listaErro = new ArrayList<ErrorObject>();
		this.httpStatus = null;
		this.mensagem = null;
		this.data = null;
	}
	
	public void preencherException(String erro) {
		this.listaErro.clear();
		this.listaErro.add(new ErrorObject(erro, null, null));
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		this.data = null;
		mensagem = "Servidor não conseguiu processar a solicitação!";
	}

}
