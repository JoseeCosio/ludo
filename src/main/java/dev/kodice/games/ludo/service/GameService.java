package dev.kodice.games.ludo.service;

import java.util.List;
import java.util.Optional;

import dev.kodice.games.ludo.domain.model.Game;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.Meeple;
import dev.kodice.games.ludo.domain.model.Player;

public interface GameService {

	Game newGame(Game game);

	Game reset(Game game);

	Optional<Game> getGameById(Long id);
	
	Game save(Game game);

	Game update(Game game);

	boolean isKeyFromGame(List<Player> players, String key);

	int getPlayerToRoll(List<Player> players);
	
	List<GameSnapshot> getSnapshot(Long gameId);
	
	void setExtraTurn(Long gameId);
	
	void removeExtraTurn(Long gameId);

	void setDice(int dice, Long id);
	
	void setRoll(Long id);

	void setMove(Long id);
	
	void updateMeeple(Meeple meeple);

	List<Game> getGames();

	List<Player> getGamePlayers(Long gameId);
}
