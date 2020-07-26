package dev.kodice.games.ludo.domain.dto;

import lombok.Data;

@Data
public class PlayerActionDto {

	private boolean sincronize;
	
	private boolean roll;
	
	private int move;
	
}
