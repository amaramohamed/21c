package de.adesso.c.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.adesso.c.entities.Versicherter;
import de.adesso.c.entities.Versicherungsgesellschaft;

public interface Versicherterrepo extends CrudRepository<Versicherter, String> {
    List<Versicherter> findByVersicherterVersicherung(Versicherungsgesellschaft versicherungsgesellschaft);
}
