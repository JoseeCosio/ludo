package dev.kodice.games.ludo.domain.dto;

import java.util.List;

import dev.kodice.games.ludo.domain.model.GameSnapshot;
import lombok.Data;

@Data
public class TurnDto {

	private int playerInTurn;
	
	private int rolled;
	
	private List<Boolean> moves;
	
	private List<MovedMeeple> movedMeeples;
	
	private String message;
	
	private List<GameSnapshot> game;
}
