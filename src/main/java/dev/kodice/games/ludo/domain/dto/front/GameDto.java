package dev.kodice.games.ludo.domain.dto.front;

import java.util.List;

import lombok.Data;

@Data
public class GameDto {
	
	private List<FrontPlayerDto> players;
	
	int playerTurn;
	
	String requiredAction;
	
	int dice;
	
}
