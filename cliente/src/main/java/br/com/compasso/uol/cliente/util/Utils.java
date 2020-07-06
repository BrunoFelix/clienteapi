package br.com.compasso.uol.cliente.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class Utils {
	
	/**
	 * Calcula a idade do cliente de acordo com a data de nascimento 
	 * @param Date data
	 * @return int
	 */
	public int calculaIdade(Date data) {

	    Calendar calendarDataNascimento = Calendar.getInstance();  
	    calendarDataNascimento.setTime(data); 
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
	
	/**
	 * Verifica se a data informada é valida, maior que 0000-00-00 
	 * @param Date data
	 * @return boolean
	 */
	public boolean validaData(Date data) throws Exception {
		if (data != null) {
			Date dataAtual = new Date();
			SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy");
			DateFormat dateFormat = DateFormat.getDateInstance();
			Date dataLimite = dateFormat.parse("00/00/0000");
			formatoData.format(dataAtual);
			
			if (data.after(dataLimite) && (data.before(dataAtual) || data.equals(dataAtual))){
				return true;
			}else {
				return false;
			}
		}
		
		return true;
	}	
}
