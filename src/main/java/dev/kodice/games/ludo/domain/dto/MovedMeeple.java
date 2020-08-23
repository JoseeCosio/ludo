package dev.kodice.games.ludo.domain.dto;

import lombok.Data;

@Data
public class MovedMeeple {

	public MovedMeeple() {
		this.playerId = 0L;
		this.meeple = 0;
		this.initialPosition = 0;
		this.finalPosition = 0;
		this.landingRelativePosition = 0;
	}

	public MovedMeeple(Long playerId, int meeple, int initialPosition, int finalPosition, int landingRelativePosition) {
		this.playerId = playerId;
		this.meeple = meeple;
		this.initialPosition = initialPosition;
		this.finalPosition = finalPosition;
		this.landingRelativePosition = landingRelativePosition;
	}

	private Long playerId;

	private int meeple;

	private int initialPosition;

	private int finalPosition;

	private int landingRelativePosition;
}
