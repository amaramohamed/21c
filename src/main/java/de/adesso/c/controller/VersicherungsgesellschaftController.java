package de.adesso.c.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.adesso.c.entities.Versicherter;
import de.adesso.c.entities.Versicherungsgesellschaft;
import de.adesso.c.interfaces.Versicherungsservice;
import de.adesso.c.repository.Versicherterrepo;

@RestController
@RequestMapping("/versicherung")
public class VersicherungsgesellschaftController {

	@Autowired
	Versicherungsservice versicherungsservice;
	
	@Autowired
	Versicherterrepo versicherterrepo;
	
	@GetMapping("/test")
	public String test() {
		Versicherter versicherter = Versicherter.builder()
				.versicherterName("Mustermann")
				.versicherterVorname("Max")
				.versicherterEmail("max.mustermann@adesso.de")
				.versicherterTelefonnummer("0049 123 456789")
				.versicherterVersicherung(versicherungsservice.filterVersicherungsgesellschaft(null, null).get(0))
				.build();
		versicherterrepo.save(versicherter);
		return "Erledigt";
	}
	
	@GetMapping("")
	public List<Versicherungsgesellschaft> filterVersicherungen(@RequestParam(name = "uuid", required = false) String uuid,
			                                                    @RequestParam(name = "name", required = false) String name) {
		return versicherungsservice.filterVersicherungsgesellschaft(uuid, name);
	}
	
	@PostMapping("")
	public ResponseEntity<Versicherungsgesellschaft> versicherungHinzufuegen(@RequestBody Versicherungsgesellschaft versicherungsgesellschaft) {
		return versicherungsservice.versicherungHinzufuegen(versicherungsgesellschaft);
	}
	
	@GetMapping("/{uuid}")
	public Versicherungsgesellschaft getVersicherungsgesellschaftByUuid(@PathVariable String uuid) {
		return versicherungsservice.getVersicherungsgesellschaftByUuid(uuid);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/{uuid}")
	public ResponseEntity deleteVersicherungsgesellschaft(@PathVariable String uuid) throws Exception  {
		return versicherungsservice.versicherungLoeschen(uuid);
	}
	
}
