package br.com.compasso.uol.cliente.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;


import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="cliente")
public class Cliente implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", nullable = false, unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome", length=60)
	@NotNull(message = "Campo nome não pode ser vazio!")
	@Size(min = 10, max = 60, message = "Campo nome precisa ter entre 10 e 60 caracteres!")
	@NotBlank
	@NotEmpty
	private String nome;
	
	@Column(name = "sexo", length=1)
	@NotNull(message = "Campo sexo não pode ser vazio!")
	@Size(min = 1, max = 1, message = "Campo sexo deve ser preencido como 'F' (Feminino) ou 'M' (Masculino)!")
	@NotBlank
	@NotEmpty
	private String sexo;
	
	@Column(name = "dt_nascimento")
	@NotNull(message = "Campo data de nascimento não pode estar vazio!")
	@PastOrPresent(message = "Campo data de nascimento não pode ser maior que a data atual!")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone="America/Sao_Paulo")
	private Date dt_nascimento;
	
	@Column(name = "idade", columnDefinition = "int")
	@NotNull(message = "Campo idade não pode estar vazio!") 
	private Integer idade;
	
	@ManyToOne
	@JoinColumn(name = "id_cidade", nullable = true, foreignKey=@ForeignKey(name="fk_cliente_cidade"))
	private Cidade cidade;

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

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Date getDt_nascimento() {
		return dt_nascimento;
	}

	public void setDt_nascimento(Date dt_nascimento) {
		this.dt_nascimento = dt_nascimento;
	}

	public Integer getIdade() {
		return idade;
	}
	
	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
}
