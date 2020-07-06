package br.com.compasso.uol.cliente.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

			// Caso já exista um registro com o mesmo nome
			if (listaCliente.size() > 0) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível inserir o Cliente!";
				this.listaErro.add(new ErrorObject("Já existe um cadastro de cliente com o nome informado!", null, null));
				this.data = null;
			} else {
				// Validando cidade
				if (cliente.getCidade() == null || validaCidade(cliente.getCidade().getId(), "inserir")) {
					// Validando data
					if (utils.validaData(cliente.getDt_nascimento())) {
						// Calculando idade com base na data de nascimento
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
			// Caso o pârametro seja inválido
			if (cliente.getId() == null || cliente.getId() <= 0) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível atualizar o registro!";
				this.listaErro.add(new ErrorObject("O ID informado é inválido!", null, null));
			} else {
				Optional<Cliente> clienteBD = clienteRep.findById(cliente.getId());

				// Caso não exista nenhum registro
				if (!clienteBD.isPresent()) {
					this.httpStatus = HttpStatus.BAD_REQUEST;
					this.mensagem = "Não foi possível atualizar o registro!";
					this.listaErro.add(new ErrorObject("O ID informado não existe na base de dados!", null, null));
				} else {
					List<Cliente> listaCliente = new ArrayList<Cliente>();
					clienteRep.findByNome(cliente.getNome()).forEach(listaCliente::add);

					//Validando novo nome inserido
					boolean nomeJaExiste = false;
					for (Cliente cliente2 : listaCliente) {
						if (!cliente2.getId().equals(cliente.getId())) {
							nomeJaExiste = true;
						}
					}
					
					if (nomeJaExiste) {
						this.httpStatus = HttpStatus.BAD_REQUEST;
						this.mensagem = "Não foi possível atualizar o Cliente!";
						this.listaErro.add(new ErrorObject("Já existe um cadastro de cliente com o nome informado!", null, null));
						this.data = null;
					} else {
						// Validando cidade
						if (cliente.getCidade() == null || validaCidade(cliente.getCidade().getId(), "inserir")) {
							// Validando data
							if (utils.validaData(cliente.getDt_nascimento())) {
								// Calculando idade com base na data de nascimento
								if (cliente.getDt_nascimento() != null) {
									cliente.setIdade(utils.calculaIdade(cliente.getDt_nascimento()));
								}

								clienteBD.get().setNome(
										cliente.getNome() == null ? clienteBD.get().getNome() : cliente.getNome());
								clienteBD.get().setSexo(
										cliente.getSexo() == null ? clienteBD.get().getSexo() : cliente.getSexo());
								clienteBD.get().setCidade(cliente.getCidade() == null ? clienteBD.get().getCidade()
										: cliente.getCidade());
								clienteBD.get().setIdade(
										cliente.getIdade() == null ? clienteBD.get().getIdade() : cliente.getIdade());
								// Formatando data
								Date dataFormatada = new SimpleDateFormat("yyyy-MM-dd")
										.parse(cliente.getDt_nascimento() == null
												? clienteBD.get().getDt_nascimento().toString()
												: cliente.getDt_nascimento().toString());
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
			// Caso o pârametro seja inválido
			if (id == null || id <= 0) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível deletar o registro!";
				this.listaErro.add(new ErrorObject("O ID informado é inválido!", null, null));
			} else {
				Optional<Cliente> cliente = clienteRep.findById(id);

				// Caso não exista nenhum registro
				if (!cliente.isPresent()) {
					this.httpStatus = HttpStatus.BAD_REQUEST;
					this.mensagem = "Não foi possível deletar o registro!";
					this.listaErro.add(new ErrorObject("O ID informado não existe na base de dados!", null, null));
				} else {
					clienteRep.deleteById(id);
					this.httpStatus = HttpStatus.OK;
					this.mensagem = "Registro deletado com sucesso!";
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
			// Caso o pârametro seja inválido
			if (id == null || id <= 0) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível pesquisar o registro!";
				this.listaErro.add(new ErrorObject("O ID informado é inválido!", null, null));
			} else {
				Optional<Cliente> cliente = clienteRep.findById(id);

				// Caso não exista nenhum registro
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
			// Caso o pârametro seja inválido
			if (nome == null || nome.trim().isEmpty()) {
				this.httpStatus = HttpStatus.BAD_REQUEST;
				this.mensagem = "Não foi possível pesquisar o registro!";
				this.listaErro.add(new ErrorObject("O nome informado é inválido!", null, null));
			} else {
				List<Cliente> listaCliente = new ArrayList<Cliente>();
				clienteRep.findByNome(nome).forEach(listaCliente::add);

				// Caso não exista nenhum registro
				if (listaCliente.size() <= 0) {
					this.httpStatus = HttpStatus.BAD_REQUEST;
					this.mensagem = "Não foi possível pesquisar o registro!";
					this.listaErro.add(new ErrorObject("O nome informado não existe na base de dados!", null, null));
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

	/**
	 * Método para limpar variáveis antes de utilizá-las
	 */
	private void limparVariaveis() {
		this.response = new Response();
		this.listaErro = new ArrayList<ErrorObject>();
		this.httpStatus = null;
		this.mensagem = null;
		this.data = null;
	}

	/**
	 * Método para padronizar exceções do Java
	 */
	public void preencherException(Exception e) {
		this.listaErro.clear();
		this.listaErro.add(new ErrorObject(e.toString(), null, null));
		this.listaErro.add(new ErrorObject(e.getMessage(), null, null));
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		this.data = null;
		mensagem = "Servidor não conseguiu processar a solicitação!";
	}

	/**
	 * Método para padronizar exceções de validações do JPA
	 */
	public void preencherValidationException(ConstraintViolationException e) {
		this.listaErro.clear();
		this.listaErro.add(new ErrorObject(e.getMessage(), null, null));
		this.httpStatus = HttpStatus.BAD_REQUEST;
		this.data = null;
		mensagem = "Os seguintes campos precisam ser ajustados!";
	}

	/**
	 * Método para validar se a cidade existe antes de inserir o cliente
	 */
	public boolean validaCidade(Long id, String acao) {
		if (id != null && id > 0) {
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
