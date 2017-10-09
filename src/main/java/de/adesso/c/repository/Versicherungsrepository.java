package de.adesso.c.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import de.adesso.c.entities.Versicherungsgesellschaft;

public interface Versicherungsrepository extends CrudRepository<Versicherungsgesellschaft, String> {
  
	@Query("SELECT v FROM Versicherungsgesellschaft v WHERE (:uuid IS NULL or v.versicherungUuid = :uuid) AND "
			+ "(:name IS NULL or v.versicherungName = :name)")
	List<Versicherungsgesellschaft> filterVersicherung(@Param("uuid") String uuid, @Param("name") String name);
	
}
