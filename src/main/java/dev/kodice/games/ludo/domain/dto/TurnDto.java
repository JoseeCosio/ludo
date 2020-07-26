package dev.kodice.games.ludo.domain.dto;

import java.util.List;

import dev.kodice.games.ludo.MovingMeeples;
import lombok.Data;

@Data
public class TurnDto {

	private String roll;
	
	private int rolled;
	
	private MovingMeeples moves;
	
	private List<MovedMeeples> movedMeeples;
	
	private String message;
}
