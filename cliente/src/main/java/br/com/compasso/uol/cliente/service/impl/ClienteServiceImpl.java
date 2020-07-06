package br.com.compasso.uol.cliente.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.compasso.uol.cliente.model.entity.Cidade;
import br.com.compasso.uol.cliente.model.entity.Cliente;
import br.com.compasso.uol.cliente.model.response.ErrorObject;
import br.com.compasso.uol.cliente.model.response.Response;
import br.com.compasso.uol.cliente.repository.CidadeRepository;
import br.com.compasso.uol.cliente.repository.ClienteRepository;
import br.com.compasso.uol.cliente.service.ClienteService;
import br.com.compasso.uol.cliente.util.Utils;

@Service
public class ClienteServiceImpl implements ClienteService {

	Response response = new Response();
	List<ErrorObject> listaErro = new ArrayList<ErrorObject>();
	HttpStatus httpStatus = null;
	String mensagem = null;
	Object data = null;

	@Autowired
	private ClienteRepository clienteRep;

	@Autowired
	private CidadeRepository cidadeRep;

	@Autowired
	private Utils utils;
	
	@Override
	public ResponseEntity<Response> add(Cliente cliente) {
		// TODO Auto-generated method stub
		limparVariaveis();
		try {
			// validacoes
			List<Cliente> listaCliente = new ArrayList<Cliente>();
			clienteRep.findByNome(cliente.getNome()).forEach(listaCliente::add);

			if (listaCliente.size() > 0) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível inserir o Cliente!";
				this.listaErro
						.add(new ErrorObject("Já existe um cadastro de cliente com o nome informado!", null, null));
				this.data = null;
			} else {
				if (cliente.getCidade() == null || validaCidade(cliente.getCidade().getId(), "inserir")) {
					if (utils.validaData(cliente.getDt_nascimento())) {
						if (cliente.getDt_nascimento() != null) {
							cliente.setIdade(utils.calculaIdade(cliente.getDt_nascimento()));
						}
					
						Cliente retornoCliente = clienteRep.save(cliente);

						if (retornoCliente != null) {
							this.httpStatus = HttpStatus.CREATED;
							this.mensagem = "Registro criado com sucesso!";
							this.data = retornoCliente;
						}
					} else {
						this.httpStatus = HttpStatus.BAD_REQUEST;
						this.mensagem = "Não foi possível criar o registro!";
						this.listaErro.add(new ErrorObject("Campo Data Inválido!", null, null));
					}
				}
			}
		} catch (ConstraintViolationException e) {
			preencherValidationException(e);
		} catch (Exception e) {
			preencherException(e);
		}

		this.response.setHttpStatus(httpStatus);
		this.response.setErrors(listaErro);
		this.response.setMessage(mensagem);
		this.response.setData(data);
		return new ResponseEntity<>(this.response, this.response.getHttpStatus());
	}

	@Override
	public ResponseEntity<Response> update(Cliente cliente) {
		// TODO Auto-generated method stub
		limparVariaveis();
		try {
			if (cliente.getId() == null || cliente.getId() <= 0) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível atualizar o registro!";
				this.listaErro.add(new ErrorObject("O ID informado é inválido!", null, null));
			} else {
				Optional<Cliente> clienteBD = clienteRep.findById(cliente.getId());

				if (!clienteBD.isPresent()) {
					this.httpStatus = HttpStatus.BAD_REQUEST;
					this.mensagem = "Não foi possível atualizar o registro!";
					this.listaErro.add(new ErrorObject("O ID informado não existe na base de dados!", null, null));
				} else {
					if (cliente.getCidade() == null || validaCidade(cliente.getCidade().getId(), "inserir")) {
						if (utils.validaData(cliente.getDt_nascimento())) {
							if (cliente.getDt_nascimento() != null) {
								cliente.setIdade(utils.calculaIdade(cliente.getDt_nascimento()));
							}
							
							clienteBD.get().setNome(cliente.getNome() == null ? clienteBD.get().getNome() : cliente.getNome());
							clienteBD.get().setSexo(cliente.getSexo() == null ? clienteBD.get().getSexo() : cliente.getSexo());
							clienteBD.get().setCidade(cliente.getCidade() == null ? clienteBD.get().getCidade() : cliente.getCidade());
							clienteBD.get().setIdade(cliente.getIdade() == null ? clienteBD.get().getIdade() : cliente.getIdade());
							Date dataFormatada=new SimpleDateFormat("dd-MM-yyyy").parse(cliente.getDt_nascimento() == null ? clienteBD.get().getDt_nascimento().toString() : cliente.getDt_nascimento().toString()); 
							clienteBD.get().setDt_nascimento(dataFormatada);
							
							Cliente retornoCliente = clienteRep.saveAndFlush(clienteBD.get());
							this.httpStatus = HttpStatus.OK;
							this.mensagem = "Registro atualizado com sucesso!";
							this.data = retornoCliente;
						} else {
							this.httpStatus = HttpStatus.BAD_REQUEST;
							this.mensagem = "Não foi possível atualizar o registro!";
							this.listaErro.add(new ErrorObject("Campo Data Inválido!", null, null));
						}
					}
				}
			}
		} catch (ConstraintViolationException e) {
			preencherValidationException(e);
		} catch (Exception e) {
			preencherException(e);
		}

		this.response.setHttpStatus(httpStatus);
		this.response.setErrors(listaErro);
		this.response.setMessage(mensagem);
		this.response.setData(data);
		return new ResponseEntity<>(this.response, this.response.getHttpStatus());
	}

	@Override
	public ResponseEntity<Response> delete(Long id) {
		// TODO Auto-generated method stub
		limparVariaveis();
		try {
			if (id == null || id <= 0) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível deletar o registro!";
				this.listaErro.add(new ErrorObject("O ID informado é inválido!", null, null));
			} else {
				Optional<Cliente> cliente = clienteRep.findById(id);

				if (!cliente.isPresent()) {
					this.httpStatus = HttpStatus.BAD_REQUEST;
					this.mensagem = "Não foi possível deletar o registro!";
					this.listaErro.add(new ErrorObject("O ID informado é inválido!", null, null));
				} else {
					clienteRep.deleteById(id);
					this.httpStatus = HttpStatus.OK;
					this.mensagem = "Cliente ID " + id + " deletado com sucesso!";
				}
			}
		} catch (ConstraintViolationException e) {
			preencherValidationException(e);
		} catch (Exception e) {
			preencherException(e);
		}
		this.response.setHttpStatus(httpStatus);
		this.response.setErrors(listaErro);
		this.response.setMessage(mensagem);
		this.response.setData(data);
		return new ResponseEntity<>(this.response, this.response.getHttpStatus());
	}

	@Override
	public ResponseEntity<Response> findById(Long id) {
		// TODO Auto-generated method stub
		limparVariaveis();
		try {
			if (id == null || id <= 0) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível pesquisar o registro!";
				this.listaErro.add(new ErrorObject("O ID informado é inválido!", null, null));
			} else {
				Optional<Cliente> cliente = clienteRep.findById(id);

				if (!cliente.isPresent()) {
					this.httpStatus = HttpStatus.BAD_REQUEST;
					this.mensagem = "Não foi possível pesquisar o registro!";
					this.listaErro.add(new ErrorObject("O ID informado não existe na base de dados!", null, null));
				} else {
					this.httpStatus = HttpStatus.OK;
					this.data = cliente.get();
				}
			}
		} catch (ConstraintViolationException e) {
			preencherValidationException(e);
		} catch (Exception e) {
			preencherException(e);
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
			} else {
				List<Cliente> listaCliente = new ArrayList<Cliente>();
				clienteRep.findByNome(nome).forEach(listaCliente::add);

				if (listaCliente.size() <= 0) {
					this.httpStatus = HttpStatus.BAD_REQUEST;
					this.mensagem = "Não foi possível pesquisar o registro!";
					this.listaErro.add(new ErrorObject("O nome informado não existe na banco de dados!", null, null));
				} else {
					this.httpStatus = HttpStatus.OK;
					this.data = listaCliente;
					this.mensagem = listaCliente.size() + (listaCliente.size() == 1 ? " Registro" : "Registros");
				}
			}
		} catch (ConstraintViolationException e) {
			preencherValidationException(e);
		} catch (Exception e) {
			preencherException(e);
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

	public void preencherException(Exception e) {
		this.listaErro.clear();
		this.listaErro.add(new ErrorObject(e.toString(), null, null));
		this.listaErro.add(new ErrorObject(e.getMessage(), null, null));	
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		this.data = null;
		mensagem = "Servidor não conseguiu processar a solicitação!";
	}
	
	public void preencherValidationException(ConstraintViolationException e) {
		this.listaErro.clear();
		this.listaErro.add(new ErrorObject(e.getMessage(), null, null));
		this.httpStatus = HttpStatus.BAD_REQUEST;
		this.data = null;
		mensagem = "Os seguintes campos precisam ser ajustados!";
	}

	public boolean validaCidade(Long id, String acao) {
		if (id != null || id > 0) {
			Optional<Cidade> cidade = cidadeRep.findById(id);

			if (!cidade.isPresent()) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível " + acao + " o registro!";
				this.listaErro
						.add(new ErrorObject("O ID da cidade informada não existe no banco de dados!", null, null));
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}

	}

}
