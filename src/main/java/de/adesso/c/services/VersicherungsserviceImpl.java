package de.adesso.c.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.adesso.c.entities.Versicherter;
import de.adesso.c.entities.Versicherungsgesellschaft;
import de.adesso.c.interfaces.Versicherungsservice;
import de.adesso.c.repository.Versicherterrepo;
import de.adesso.c.repository.Versicherungsrepository;
import javassist.tools.rmi.ObjectNotFoundException;

@Service
public class VersicherungsserviceImpl implements Versicherungsservice {

	@Autowired
	Versicherungsrepository versicherungsrepository;

	@Autowired
	Versicherterrepo versicherterrepo;

	/* (non-Javadoc)
	 * @see de.adesso.c.services.Versicherungsservice#versicherungHinzufuegen(de.adesso.c.entities.Versicherungsgesellschaft)
	 */
	@Override
	public ResponseEntity<Versicherungsgesellschaft> versicherungHinzufuegen(
			Versicherungsgesellschaft versicherungsgesellschaft) {
		Versicherungsgesellschaft createdVersicherungsgesellschaft = versicherungsrepository
				.save(versicherungsgesellschaft);
		return new ResponseEntity<Versicherungsgesellschaft>(createdVersicherungsgesellschaft, HttpStatus.CREATED);
	}
    
	/* (non-Javadoc)
	 * @see de.adesso.c.services.Versicherungsservice#filterVersicherungsgesellschaft(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Versicherungsgesellschaft> filterVersicherungsgesellschaft(String uuid, String name) {
		return versicherungsrepository.filterVersicherung(uuid, name);
	}
	
	/* (non-Javadoc)
	 * @see de.adesso.c.services.Versicherungsservice#getVersicherungsgesellschaftByUuid(java.lang.String)
	 */
	@Override
	public Versicherungsgesellschaft getVersicherungsgesellschaftByUuid(String versicherungsgesellschaftUuid) {
		return versicherungsrepository.findOne(versicherungsgesellschaftUuid);
	}

	/* (non-Javadoc)
	 * @see de.adesso.c.services.Versicherungsservice#versicherungLoeschen(java.lang.String)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public ResponseEntity versicherungLoeschen(String versicherungsgesellschaftUuid) throws Exception {
		Versicherungsgesellschaft versicherungsgesellschaft = versicherungsrepository
				.findOne(versicherungsgesellschaftUuid);
		if (versicherungsgesellschaft == null) {
			throw new ObjectNotFoundException("Die Versicherungsgesellschaft mit dem ID: "
					+ versicherungsgesellschaftUuid + " wurde nicht gefunden");
		} else {
			List<Versicherter> versicherter = versicherterrepo
					.findByVersicherterVersicherung(versicherungsgesellschaft);
			if (versicherter.isEmpty()) {
				versicherungsrepository.delete(versicherungsgesellschaftUuid);
			} else {
				throw new Exception("Sie müssen erstmal die Mitglieder der Versicherungsgesellschaft "
						+ versicherungsgesellschaft.getVersicherungName() + " einer anderen Versicherung zuweisen.");
			}
		}
		return new ResponseEntity(HttpStatus.OK);
	}

}
