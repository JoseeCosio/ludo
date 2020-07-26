package dev.kodice.games.ludo;

import java.util.List;

import dev.kodice.games.ludo.domain.dto.MovedMeeples;
import dev.kodice.games.ludo.domain.model.GameState;
import dev.kodice.games.ludo.domain.model.Meeple;
import dev.kodice.games.ludo.domain.model.Player;
import lombok.Data;

@Data
public class PlayerAndLand {

	private GameState gameState;
	
	private Player player;
	
	private Meeple initialMeeple;
	
	private Meeple finalMeeple;
	
	private List<MovedMeeples> movedMeeples;
	
	private boolean moved;
	

}
