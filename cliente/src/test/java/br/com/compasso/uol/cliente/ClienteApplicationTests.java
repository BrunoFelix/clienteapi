package br.com.compasso.uol.cliente;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
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
import br.com.compasso.uol.cliente.model.entity.Cliente;
import br.com.compasso.uol.cliente.util.GsonUTCDateAdapter;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ClienteApplicationTests {

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
		String url = "/api/cliente/add/";

		Cliente cliente = new Cliente();
		cliente.setNome("Bruno Felix");
		cliente.setSexo("M");
		cliente.setIdade(25);
		String dataPadrao = "19-01-1995";
		Date dataNascimento = new SimpleDateFormat("dd-MM-yyyy").parse(dataPadrao);
		cliente.setDt_nascimento(dataNascimento);

		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();

		ResultActions response = this.mockMvc
				.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cliente)));

		response.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Registro criado com sucesso!"))
				.andExpect(jsonPath("data").exists());
	}

	@Test
	public void testIncluirClienteComCidade() throws Exception {
		String url = "/api/cliente/add/";

		String urlCidade = "/api/cidade/add/";

		Cliente cliente = new Cliente();

		cliente.setNome("João Maria");
		cliente.setSexo("M");
		cliente.setIdade(25);
		String dataPadrao = "12-07-2005";
		Date dataNascimento = new SimpleDateFormat("dd-MM-yyyy").parse(dataPadrao);
		cliente.setDt_nascimento(dataNascimento);

		Cidade cidade = new Cidade();
		cidade.setId(new Long(1));
		cidade.setEstado("Teste");
		cidade.setNome("Teste");
		cliente.setCidade(cidade);

		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();

		this.mockMvc.perform(post(urlCidade).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cidade)));

		ResultActions response = this.mockMvc
				.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cliente)));

		response.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Registro criado com sucesso!"))
				.andExpect(jsonPath("data").exists());
	}

	/*
	 * Testes para atualizar
	 */
	@Test
	public void testAtualizarNomeCliente() throws Exception {
		String url = "/api/cliente/add/";

		Cliente cliente = new Cliente();
		cliente.setNome("Bruno Teste Atualizacao");
		cliente.setSexo("M");
		cliente.setIdade(25);
		String dataPadrao = "19-01-1995";
		Date dataNascimento = new SimpleDateFormat("dd-MM-yyyy").parse(dataPadrao);
		cliente.setDt_nascimento(dataNascimento);

		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();

		ResultActions response = this.mockMvc
				.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cliente)));

		url = "/api/cliente/update/";

		cliente = new Cliente();
		cliente.setId(new Long(6));
		cliente.setNome("Bruno Novo Nome");

		gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();

		response = this.mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cliente)));

		response.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Registro atualizado com sucesso!"))
				.andExpect(jsonPath("data.nome").value("Bruno Novo Nome"));
	}

	/*
	 * Testes para deletar
	 */
	@Test
	public void testDeletaClienteSemParametroID() throws Exception {
		String url = "/api/cliente/delete/null";

		ResultActions response = this.mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isBadRequest());
	}

	@Test
	public void testDeletaClienteParametroIDZerado() throws Exception {
		String url = "/api/cliente/delete/0";

		ResultActions response = this.mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Não foi possível deletar o registro!"))
				.andExpect(jsonPath("errors[0].message").value("O ID informado é inválido!"));
	}

	@Test
	public void testDeletaClienteComIDInexistente() throws Exception {
		String url = "/api/cliente/delete/99";

		ResultActions response = this.mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Não foi possível deletar o registro!"))
				.andExpect(jsonPath("errors[0].message").value("O ID informado não existe na base de dados!"));
	}

	@Test
	public void testDeletaClienteComIDExistente() throws Exception {
		String url = "/api/cliente/add";

		Cliente cliente = new Cliente();
		cliente.setNome("Bruno Teste");
		cliente.setSexo("M");
		cliente.setIdade(25);
		String dataPadrao = "19-01-1995";
		Date dataNascimento = new SimpleDateFormat("dd-MM-yyyy").parse(dataPadrao);
		cliente.setDt_nascimento(dataNascimento);

		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();

		ResultActions response = this.mockMvc
				.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cliente)));

		url = "/api/cliente/delete/1";

		gson = new Gson();

		response = this.mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Registro deletado com sucesso!"));
	}

	/*
	 * Testes para consulta
	 */
	@Test
	public void testConsultaClienteSemParametroID() throws Exception {
		String url = "/api/cliente/id/null";

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isBadRequest());
	}

	@Test
	public void testConsultaClienteParametroIDZerado() throws Exception {
		String url = "/api/cliente/id/0";

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Não foi possível pesquisar o registro!"))
				.andExpect(jsonPath("errors[0].message").value("O ID informado é inválido!"));
	}

	@Test
	public void testConsultaClienteComIDInexistente() throws Exception {
		String url = "/api/cliente/id/99";

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Não foi possível pesquisar o registro!"))
				.andExpect(jsonPath("errors[0].message").value("O ID informado não existe na base de dados!"));
	}

	@Test
	public void testConsultaClienteComIDExistente() throws Exception {
		String url = "/api/cliente/add";

		Cliente cliente = new Cliente();
		cliente.setNome("Bruno Novo");
		cliente.setSexo("M");
		cliente.setIdade(25);
		String dataPadrao = "19-01-1995";
		Date dataNascimento = new SimpleDateFormat("dd-MM-yyyy").parse(dataPadrao);
		cliente.setDt_nascimento(dataNascimento);

		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();

		ResultActions response = this.mockMvc
				.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cliente)));

		url = "/api/cliente/id/2";

		gson = new Gson();

		response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("data").exists());
	}

	@Test
	public void testConsultaClienteSemParametroNome() throws Exception {
		String url = "/api/cliente/nome/null";

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isBadRequest());
	}

	@Test
	public void testConsultaClienteComNomeInexistente() throws Exception {
		String url = "/api/cliente/nome/bruno barbosa";

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Não foi possível pesquisar o registro!"))
				.andExpect(jsonPath("errors[0].message").value("O nome informado não existe na base de dados!"));
	}

	@Test
	public void testConsultaClienteComNomeExistente() throws Exception {
		String url = "/api/cliente/add/";

		Cliente cliente = new Cliente();
		cliente.setNome("Bruno Teste");
		cliente.setSexo("M");
		cliente.setIdade(25);
		String dataPadrao = "19-01-1995";
		Date dataNascimento = new SimpleDateFormat("dd-MM-yyyy").parse(dataPadrao);
		cliente.setDt_nascimento(dataNascimento);

		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();

		ResultActions response = this.mockMvc
				.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cliente)));

		url = "/api/cliente/nome/Bruno Teste";

		gson = new Gson();

		response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));

		response.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("data").exists());
	}
}
