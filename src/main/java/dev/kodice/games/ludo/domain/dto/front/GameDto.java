package dev.kodice.games.ludo.domain.dto.front;

import java.util.List;

import lombok.Data;

@Data
public class GameDto {
	
	private int playerTurn;
	
	private String requiredAction;
	
	private int dice;
	
	private List<FrontPlayerDto> players;

	public GameDto(int playerTurn, String requiredAction, int dice, List<FrontPlayerDto> players) {
		this.playerTurn = playerTurn;
		this.requiredAction = requiredAction;
		this.dice = dice;
		this.players = players;
	}
	
}
