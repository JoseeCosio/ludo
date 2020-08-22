package dev.kodice.games.ludo.domain.dto;

import java.util.ArrayList;
import java.util.List;

import dev.kodice.games.ludo.domain.dto.front.FrontPlayerDto;
import dev.kodice.games.ludo.domain.dto.front.GameDto;
import lombok.Data;

@Data
public class TurnDto {

	private int playerInTurn;

	private int dice;

	private List<Boolean> moves;

	private List<MovedMeeple> movedMeeples;

	private String message;

	private GameDto game;

	public TurnDto() {
		this.playerInTurn = 0;
		this.dice = 0;
		List<Boolean> boolMoves =new ArrayList<Boolean>();
		for(int i=0;i<=3;i++) {
			boolMoves.add(false);
		}
		this.moves = boolMoves;
		List<MovedMeeple> moved = new ArrayList<MovedMeeple>();
		moved.add(new MovedMeeple(0L,0,0,0));
		this.movedMeeples = moved;
		this.message = "";
		List<FrontPlayerDto> front = new ArrayList<FrontPlayerDto>();
		List<Long> meeples = new ArrayList<Long>();
		for(int i=0;i<=3;i++) {
			meeples.add(0,0L);
		}
		for(int i=0;i<=3;i++) {
			front.add(new FrontPlayerDto(meeples));
		}
		this.game = new GameDto(0,"",0,front);
	}

}
