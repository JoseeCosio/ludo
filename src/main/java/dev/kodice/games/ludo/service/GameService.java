package dev.kodice.games.ludo.service;

import java.util.Optional;

import dev.kodice.games.ludo.domain.model.Game;
import dev.kodice.games.ludo.domain.model.GameState;

public interface GameService {

	Game newGame(Game game);

	Optional<Game> getGameById(Long id);

	Game save(Game game);

	Game update(Game game);

	GameState reset(GameState gameState);

	boolean isKeyFromGame(GameState gameState, String key);

	String getPlayerToRoll(GameState gameState);
}
