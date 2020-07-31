package dev.kodice.games.ludo.domain.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameStateDto {
	
	private Long id;

	private List<PlayerDto> players;
}
