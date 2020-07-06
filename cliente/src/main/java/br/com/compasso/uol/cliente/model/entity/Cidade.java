package br.com.compasso.uol.cliente.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="cidade")
public class Cidade implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", nullable = false, unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome", length=30)
	@NotNull(message = "Campo nome não pode ser vazio!")
	@Size(min = 2, max = 30, message = "Campo nome precisa ter entre 2 e 30 caracteres!")
	@NotBlank
	@NotEmpty
	private String nome;
	
	@Column(name = "estado", length=30)
	@NotNull(message = "Campo estado não pode ser vazio!")
	@Size(min = 2, max = 30, message = "Campo nome precisa ter entre 2 e 30 caracteres!")
	@NotBlank
	@NotEmpty
	private String estado;

	/**
	 * Getters and Setters
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
