package dev.kodice.games.ludo;

import lombok.Data;

@Data
public class Game {

	private GameState gameState;
	
	private String status; //started or finished

	public Game() {
		this.gameState = new GameState();
	}
	
	
}
