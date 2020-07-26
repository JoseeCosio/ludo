package dev.kodice.games.ludo.domain.dto;

import lombok.Data;

@Data
public class MovedMeeples {

	private Long playerId;
	
	private int meeple;
	
	private int initialPosition;
	
	private int finalPosition;
}
