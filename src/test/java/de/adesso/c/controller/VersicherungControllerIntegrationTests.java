package de.adesso.c.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.adesso.c.entities.Versicherungsgesellschaft;
import de.adesso.c.repository.Versicherungsrepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VersicherungControllerIntegrationTests {

	@Autowired
	WebApplicationContext context;

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	@Autowired
	Versicherungsrepository versicherungsrepo;

	@SuppressWarnings("rawtypes")
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);
	}

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@After
	public void cleanup() {
		versicherungsrepo.deleteAll();
	}

	@Test
	public void filterVersicherungen() throws Exception {
		this.mockMvc.perform(get("/versicherung")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				// Erstes Objekt testen
				.andExpect(jsonPath("$[0].versicherungName", is("Barmenia")))
				.andExpect(jsonPath("$[0].versicherungEmail", is("info@barmenia.de")))
				.andExpect(jsonPath("$[0].versicherungTelefonnummer", is("0049 111 11111")))
				// Zweites Objekt testen
				.andExpect(jsonPath("$[1].versicherungName", is("Allianz")))
				.andExpect(jsonPath("$[1].versicherungEmail", is("info@allianz.de")))
				.andExpect(jsonPath("$[1].versicherungTelefonnummer", is("0049 222 22222")))
				// Drittes Objekt testen
				.andExpect(jsonPath("$[2].versicherungName", is("TK")))
				.andExpect(jsonPath("$[2].versicherungEmail", is("info@tk.de")))
				.andExpect(jsonPath("$[2].versicherungTelefonnummer", is("0049 333 33333")));
	}

	@Test
	public void filterVersicherungMitUuid() throws Exception {
		Versicherungsgesellschaft versicherungsgesellschaft = versicherungsrepo.save(createJpaVersicherung());
		this.mockMvc.perform(get("/versicherung?uuid=".concat(versicherungsgesellschaft.getVersicherungUuid())))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$[0].versicherungUuid", is(versicherungsgesellschaft.getVersicherungUuid())))
				.andExpect(jsonPath("$[0].versicherungName", is(versicherungsgesellschaft.getVersicherungName())))
				.andExpect(jsonPath("$[0].versicherungEmail", is(versicherungsgesellschaft.getVersicherungEmail())))
				.andExpect(jsonPath("$[0].versicherungTelefonnummer",
						is(versicherungsgesellschaft.getVersicherungTelefonnummer())));
	}

	@Test
	public void filterVersicherungMitName() throws Exception {
		Versicherungsgesellschaft versicherungsgesellschaft = versicherungsrepo.save(createJpaVersicherung());
		this.mockMvc.perform(get("/versicherung?name=".concat(versicherungsgesellschaft.getVersicherungName())))
				.andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$[0].versicherungUuid", is(versicherungsgesellschaft.getVersicherungUuid())))
				.andExpect(jsonPath("$[0].versicherungName", is(versicherungsgesellschaft.getVersicherungName())))
				.andExpect(jsonPath("$[0].versicherungEmail", is(versicherungsgesellschaft.getVersicherungEmail())))
				.andExpect(jsonPath("$[0].versicherungTelefonnummer",
						is(versicherungsgesellschaft.getVersicherungTelefonnummer())));
	}

	@Test
	public void versicherungHinzufuegen() throws Exception {
		Versicherungsgesellschaft versicherungsgesellschaft = createJpaVersicherung();
		String versicherungString = json(versicherungsgesellschaft);
		this.mockMvc.perform(post("/versicherung").content(versicherungString).contentType(contentType))
				.andExpect(status().isCreated())
//				.andExpect(jsonPath("$.versicherungUuid").isNotEmpty())
				.andExpect(jsonPath("$.versicherungName", is(versicherungsgesellschaft.getVersicherungName())))
				.andExpect(jsonPath("$.versicherungEmail", is(versicherungsgesellschaft.getVersicherungEmail())))
				.andExpect(jsonPath("$.versicherungTelefonnummer", is(versicherungsgesellschaft.getVersicherungTelefonnummer())));

	}

	Versicherungsgesellschaft createJpaVersicherung() {
		DataFactory dataFactory = new DataFactory();
		return Versicherungsgesellschaft.builder().versicherungName(dataFactory.getBusinessName())
				.versicherungEmail(dataFactory.getEmailAddress())
				.versicherungTelefonnummer(dataFactory.getNumberText(8)).build();
	}

	/* Json Konvertierung */
	@SuppressWarnings("unchecked")
	public String json(Object o) throws HttpMessageNotWritableException, IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}
