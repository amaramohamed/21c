package de.adesso.c.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import de.adesso.c.entities.Versicherungsgesellschaft;

public interface Versicherungsservice {

	ResponseEntity<Versicherungsgesellschaft> versicherungHinzufuegen(
			Versicherungsgesellschaft versicherungsgesellschaft);

	List<Versicherungsgesellschaft> filterVersicherungsgesellschaft(String uuid, String name);

	Versicherungsgesellschaft getVersicherungsgesellschaftByUuid(String versicherungsgesellschaftUuid);

	ResponseEntity versicherungLoeschen(String versicherungsgesellschaftUuid) throws Exception;

}