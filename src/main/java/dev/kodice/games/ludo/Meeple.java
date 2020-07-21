package dev.kodice.games.ludo;

import lombok.Data;

@Data
public class Meeple {

	private int position;

	private int relativePosition;

	public Meeple() {
		this.position = 0;
		this.relativePosition = 0;
	}

}
