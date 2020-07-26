package dev.kodice.games.ludo.domain.dto;

import lombok.Data;

@Data
public class GameStateDto {

	private Long id;

	private PlayerDto redPlayer;

	private PlayerDto bluePlayer;

	private PlayerDto greenPlayer;

	private PlayerDto yellowPlayer;
}
