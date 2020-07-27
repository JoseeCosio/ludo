package dev.kodice.games.ludo.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class GameStateDto {
	
	private Long id;

	private List<PlayerDto> players;
}
