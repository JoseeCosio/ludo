package dev.kodice.games.ludo.domain.dto;

import lombok.Data;

@Data
public class RegisterDto {

	private Long playerId;
	
	private String playerName;
}
