package br.com.compasso.uol.cliente.model.entity;

import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;


@Entity
@Table(name="cliente")
public class Cliente {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", nullable = false, unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome")
	@NotNull(message = "Campo nome não pode ser vazio!")
	@Size(min = 10, max = 60, message = "Campo nome precisa ter entre 10 e 60 caracteres!")
	private String nome;
	
	@Column(name = "sexo")
	@NotNull(message = "Campo sexo não pode ser vazio!")
	@Size(min = 1, max = 1, message = "Campo sexo deve ser preencido como 'F' (Feminino) ou 'M' (Masculino)!")
	private String sexo;
	
	@Column(name = "dt_nascimento", columnDefinition = "date")
	@NotNull(message = "Campo data de nascimento não pode estar vazio!")
	@Temporal(TemporalType.DATE) 
	private Date dt_nascimento;
	
	@JsonInclude()
	@Transient
	private Integer idade;
	
	@ManyToOne
	@JoinColumn(name = "id_cidade", nullable = true, foreignKey=@ForeignKey(name="fk_cliente_cidade"))
	private Cidade cidade;

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
		if (getDt_nascimento() != null) {
			return calculaIdade(getDt_nascimento());
		} else {
			return idade;
		}
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
	
	private static int calculaIdade(Date dt_nascimento) {

	    Calendar calendarDataNascimento = Calendar.getInstance();  
	    calendarDataNascimento.setTime(dt_nascimento); 
	    Calendar hoje = Calendar.getInstance();  

	    int idade = hoje.get(Calendar.YEAR) - calendarDataNascimento.get(Calendar.YEAR); 

	    if (hoje.get(Calendar.MONTH) < calendarDataNascimento.get(Calendar.MONTH)) {
	      idade--;  
	    } 
	    else 
	    { 
	        if (hoje.get(Calendar.MONTH) == calendarDataNascimento.get(Calendar.MONTH) && hoje.get(Calendar.DAY_OF_MONTH) < calendarDataNascimento.get(Calendar.DAY_OF_MONTH)) {
	            idade--; 
	        }
	    }

	    return idade;
	}
}
