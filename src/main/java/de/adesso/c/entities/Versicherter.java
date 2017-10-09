package de.adesso.c.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@Entity
public class Versicherter {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(unique = true)
	private String versicherterUuid;
	
	private String versicherterName;
	
	private String versicherterVorname;
	
	private String versicherterEmail;
	
	private String versicherterTelefonnummer;
	
	@ManyToOne
	private Versicherungsgesellschaft versicherterVersicherung;
	
	
	
}
