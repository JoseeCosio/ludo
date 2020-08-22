package dev.kodice.games.ludo.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovedMeeple {

	public MovedMeeple (Long playerId, int meeple, int initialPosition, int finalPosition) {
		this.playerId = 1L;
		this.meeple = 0;
		this.initialPosition = 0;
		this.finalPosition = 0;
	}

	private Long playerId;

	private int meeple;

	private int initialPosition;

	private int finalPosition;
}
