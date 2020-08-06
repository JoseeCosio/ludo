package dev.kodice.games.ludo.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class PlayerDto {

	private Long id;
	
	private List<Long> meeples;

	public PlayerDto(Long id) {
		this.id = id;
	}
	
}
