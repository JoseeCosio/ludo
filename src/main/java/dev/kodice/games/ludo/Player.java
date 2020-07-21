package dev.kodice.games.ludo;

import lombok.Data;

@Data
public class Player {

	private Meeple Meeple1;
	private Meeple Meeple2;
	private Meeple Meeple3;
	private Meeple Meeple4;
	private Boolean turn;

	public Player() {
		this.Meeple1 = new Meeple();
		this.Meeple2 = new Meeple();
		this.Meeple3 = new Meeple();
		this.Meeple4 = new Meeple();
		this.turn = false;
	}

}
