package dev.kodice.games.ludo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovedMeeple {

	public MovedMeeple() {
		// TODO Auto-generated constructor stub
	}

	private Long playerId;

	private int meeple;

	private int initialPosition;

	private int finalPosition;
}
