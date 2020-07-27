package dev.kodice.games.ludo.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class TurnDto {

	private int playerInTurn;
	
	private int rolled;
	
	private List<Boolean> moves;
	
	private List<MovedMeeple> movedMeeples;
	
	private String message;
}
