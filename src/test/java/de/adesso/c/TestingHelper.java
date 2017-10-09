package de.adesso.c;

import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;

import de.adesso.c.entities.Versicherungsgesellschaft;
import de.adesso.c.interfaces.Versicherungsservice;

public class TestingHelper {

	public static void mockVersicherungService(Versicherungsservice versicherungsService) {
		when(versicherungsService.filterVersicherungsgesellschaft(null, null))
			.thenReturn(Lists.newArrayList(Versicherungsgesellschaft.builder()
					.versicherungUuid("versicherung-uuid")
					.versicherungName("TestVersicherungsName")
					.versicherungEmail("info@versicherung.de")
					.versicherungTelefonnummer("0049 111 11111")
					.build()));
	}
	
	
}
