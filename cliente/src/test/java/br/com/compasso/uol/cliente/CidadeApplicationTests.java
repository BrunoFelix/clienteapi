package br.com.compasso.uol.cliente;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.compasso.uol.cliente.model.entity.Cidade;
import br.com.compasso.uol.cliente.util.GsonUTCDateAdapter;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CidadeApplicationTests {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	/*
	 * Testes de inclusão
	 */
	@Test
	public void testIncluirClienteSemCidade() throws Exception {
		String url = "/api/cidade/add/";

		Cidade cidade = new Cidade();
		cidade.setEstado("PE");
		cidade.setNome("Recife");
		
		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();

		ResultActions response = this.mockMvc
				.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cidade)));

		response.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Registro criado com sucesso!"))
				.andExpect(jsonPath("data").exists());
	}
	/*
	 * Testes para consulta
	 */
	/*
	 * Testes para consulta
	 */
	@Test
	public void testConsultaClienteSemParametroEstado() throws Exception {
		String url = "/api/cidade/estado/null";

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isBadRequest());
	}

	@Test
	public void testConsultaClienteComEstadoInexistente() throws Exception {
		String url = "/api/cidade/estado/Estado Teste";

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Não foi possível pesquisar o registro!"))
				.andExpect(jsonPath("errors[0].message").value("O estado informado não existe na base de dados!"));
	}

	@Test
	public void testConsultaClienteComEstadoExistente() throws Exception {
		String url = "/api/cidade/add/";

		Cidade cidade = new Cidade();
		cidade.setEstado("PE");
		cidade.setNome("Madalena");

		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();

		ResultActions response = this.mockMvc
				.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cidade)));

		url = "/api/cidade/estado/PE";

		gson = new Gson();

		response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("data").exists());
	}

	@Test
	public void testConsultaClienteSemParametroNome() throws Exception {
		String url = "/api/cidade/nome/null";
		
		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isBadRequest());
	}

	@Test
	public void testConsultaClienteComNomeInexistente() throws Exception {
		String url = "/api/cidade/nome/Cidade Teste";

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Não foi possível pesquisar o registro!"))
				.andExpect(jsonPath("errors[0].message").value("O nome informado não existe na base de dados!"));
	}

	@Test
	public void testConsultaClienteComNomeExistente() throws Exception {
		String url = "/api/cidade/add/";

		Cidade cidade = new Cidade();
		cidade.setEstado("PE");
		cidade.setNome("Imbiribeira");

		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();

		ResultActions response = this.mockMvc
				.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cidade)));

		url = "/api/cidade/nome/Imbiribeira";

		gson = new Gson();

		response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("data").exists());
	}

}
