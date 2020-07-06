package br.com.compasso.uol.cliente.model.response;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * Classe responsável por padronizar a resposta das requisições
 * @author Bruno
 *
 */
public class Response implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Status HTTP da resposta
	 */
	private HttpStatus httpStatus;
	
	/**
	 * Mensagem
	 */
	private String message;
	
	/**
	 * Lista de erros
	 */
	private List<ErrorObject> errors;
	
	/**
	 * Objeto
	 */
	private Object data;

	/**
	 * Getters and Setters
	 */
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String mensagem) {
		this.message = mensagem;
	}

	public List<ErrorObject> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorObject> errors) {
		this.errors = errors;
	} 
}
