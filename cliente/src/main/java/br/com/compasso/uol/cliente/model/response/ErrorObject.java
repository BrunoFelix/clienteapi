package br.com.compasso.uol.cliente.model.response;

import java.io.Serializable;

/**
 * Classe responsável pela formatação dos erros e validações
 * @author Bruno
 *
 */
public class ErrorObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final String message;
    private final String field;
    private final Object parameter;
    
    public ErrorObject (String message, String field, Object parameter) {
    	this.message = message;
    	this.field = field;
    	this.parameter = parameter;
    }

	public String getMessage() {
		return message;
	}

	public String getField() {
		return field;
	}

	public Object getParameter() {
		return parameter;
	}
}
