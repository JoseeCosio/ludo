package dev.kodice.games.ludo.domain.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "GAME_GAME_STATE", joinColumns = {
			@JoinColumn(name = "game_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "game_state_id", referencedColumnName = "id") })
	private GameState gameState;

	public Game() {
		this.gameState = new GameState();
	}

}
