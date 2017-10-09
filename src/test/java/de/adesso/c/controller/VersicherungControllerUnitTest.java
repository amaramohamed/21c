package de.adesso.c.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import de.adesso.c.TestingHelper;
import de.adesso.c.interfaces.Versicherungsservice;

@SpringBootTest
@RunWith(SpringRunner.class)
public class VersicherungControllerUnitTest {

    private MockMvc mockMvc;
	
	private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@SuppressWarnings("rawtypes")
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny()
                .orElse(null);
        assertNotNull("the JSON Message converter must not be null",
        		this.mappingJackson2HttpMessageConverter);
    }
    
    @Mock
    Versicherungsservice versicherungsservice;
    
    @InjectMocks
    private VersicherungsgesellschaftController versicherungController;
    
    @Before
    public void setup() {
    	MockitoAnnotations.initMocks(this);
    	this.mockMvc = standaloneSetup(versicherungController).build();
    	TestingHelper.mockVersicherungService(versicherungsservice);
    }
    
    @Test
    public void filterVersicherungen() throws Exception {
    	this.mockMvc.perform(get("/versicherung")).andExpect(status().isOk()).andExpect(content().contentType(contentType))
    		.andExpect(jsonPath("$[0].versicherungUuid", is("versicherung-uuid")))
    		.andExpect(jsonPath("$[0].versicherungName", is("TestVersicherungsName")))
    		.andExpect(jsonPath("$[0].versicherungEmail", is("info@versicherung.de")))
    		.andExpect(jsonPath("$[0].versicherungTelefonnummer", is("0049 111 11111")));
    }
	
}
