package br.com.compasso.uol.cliente;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import br.com.compasso.uol.cliente.model.entity.Cidade;
import br.com.compasso.uol.cliente.model.entity.Cliente;

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
	
	ObjectMapper mapper = new ObjectMapper();

	/*
	 * Testes de inclusão
	 */
	/*@Test
	 public void testIncluirClienteComTodosCamposNull() throws Exception {
	        Cliente cliente = new Cliente();
	        Gson gson = new Gson();
	        String json = gson.toJson(cliente);
	        
	        this.mockMvc.perform(get("/api/cliente/add")
	        		.contentType(MediaType.APPLICATION_JSON)
	        		.content(json))
	            .andExpect(status().isBadRequest());
	 }*/
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
	
		ResultActions responseCidade = this.mockMvc.perform(post(urlCidade).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cidade)));
		
		ResultActions response = this.mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(gson.toJson(cliente)));

		response.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("message").value("Registro criado com sucesso!"))
		.andExpect(jsonPath("data").exists());
	}
	
	/*
	 * Testes para consulta
	 */
	@Test
	public void testConsultaClienteSemParametroID() throws Exception {
		String url = "/api/cliente/id/null";

		Gson gson = new Gson();

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
		
		response.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testConsultaClienteParametroIDZerado() throws Exception {
		String url = "/api/cliente/id/0";

		Gson gson = new Gson();

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
		
		response.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Não foi possível pesquisar o registro!"))
				.andExpect(jsonPath("errors[0].message").value("O ID informado é inválido!"));
	}
	
	@Test
	public void testConsultaClienteComIDInexistente() throws Exception {
		String url = "/api/cliente/id/99";

		Gson gson = new Gson();

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
		
		response.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Não foi possível pesquisar o registro!"))
				.andExpect(jsonPath("errors[0].message").value("O ID informado não existe na base de dados!"));
	}
	
	@Test
	public void testConsultaClienteComIDExistente() throws Exception {
		String url = "/api/cliente/id/1";

		Gson gson = new Gson();

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
		
		response.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("data").exists());
	}
	
	@Test
	public void testConsultaClienteSemParametroNome() throws Exception {
		String url = "/api/cliente/nome/null";

		Gson gson = new Gson();

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
		
		response.andExpect(status().isBadRequest());
	}
	
	
	/*@Test
	public void testConsultaClienteComNomeInexistente() throws Exception {
		String url = "/api/cliente/nome/Bruno Felix";

		Gson gson = new Gson();

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
		
		response.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("message").value("Não foi possível pesquisar o registro!"))
				.andExpect(jsonPath("errors[0].message").value("O ID informado não existe na base de dados!"));
	}
	
	@Test
	public void testConsultaClienteComIDExistente() throws Exception {
		String url = "/api/cliente/id/1";

		Gson gson = new Gson();

		ResultActions response = this.mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON));
		
		response.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("data").exists());
	}*/
	
	
	// this class can't be static
	public class GsonUTCDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

		private final DateFormat dateFormat;

		public GsonUTCDateAdapter() {
			dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US); // This is the format I need
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // This is the key line which converts the date to UTC
																	// which cannot be accessed with the default
																	// serializer
		}

		@Override
		public synchronized JsonElement serialize(Date date, Type type,
				JsonSerializationContext jsonSerializationContext) {
			return new JsonPrimitive(dateFormat.format(date));
		}

		@Override
		public synchronized Date deserialize(JsonElement jsonElement, Type type,
				JsonDeserializationContext jsonDeserializationContext) {
			try {
				return dateFormat.parse(jsonElement.getAsString());
			} catch (ParseException e) {
				throw new JsonParseException(e);
			}
		}
	}

}
