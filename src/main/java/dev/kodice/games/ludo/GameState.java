package dev.kodice.games.ludo;

import lombok.Data;

@Data
public class GameState {

	private Player redPlayer;
	private Player bluePlayer;
	private Player greenPlayer;
	private Player yellowPlayer;
	private boolean extraTurn;

	public GameState() {
		Player player = new Player();
		player.setTurn(true);
		this.redPlayer = player;
		this.bluePlayer = new Player();
		this.greenPlayer = new Player();
		this.yellowPlayer = new Player();
		this.extraTurn = false;
	}

}
