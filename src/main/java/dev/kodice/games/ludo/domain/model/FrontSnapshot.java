package dev.kodice.games.ludo.domain.model;

import lombok.Data;

@Data
public class FrontSnapshot {

	private boolean sRoll;

	private int sRolled;

	private boolean sMove;

	private boolean pTurn;

	private int mPos;
	
}
