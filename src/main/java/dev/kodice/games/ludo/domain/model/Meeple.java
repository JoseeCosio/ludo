package dev.kodice.games.ludo.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Meeple {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int position;

	private int relativePosition;

	public Meeple() {
		this.position = 0;
		this.relativePosition = 0;
	}
	
}
