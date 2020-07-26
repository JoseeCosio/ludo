package dev.kodice.games.ludo.service.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.kodice.games.ludo.domain.model.Game;
import dev.kodice.games.ludo.domain.model.GameState;
import dev.kodice.games.ludo.domain.model.Meeple;
import dev.kodice.games.ludo.domain.model.Player;
import dev.kodice.games.ludo.repository.GameRepository;
import dev.kodice.games.ludo.service.GameService;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	GameRepository gameRepository;

	@Override
	public Game newGame(Game game) {
		return gameRepository.save(game);
	}

	@Override
	public Optional<Game> getGameById(Long id) {
		return gameRepository.findById(id);
	}

	@Override
	public Game save(Game game) {
		return gameRepository.save(game);
	}

	@Override
	public Game update(Game game) {
		return gameRepository.save(game);
	}

	@Override
	public GameState reset(GameState gameState) {
		Player[] players = new Player[] { gameState.getRedPlayer(), gameState.getBluePlayer(),
				gameState.getGreenPlayer(), gameState.getYellowPlayer() };
		Random rand = new Random();
		int turn = rand.nextInt(4) + 1;
		int meeple = 1;
		for (Player p : players) {
			p.setMeeple1(this.resetMeeple(p.getMeeple1()));
			p.setMeeple2(this.resetMeeple(p.getMeeple2()));
			p.setMeeple3(this.resetMeeple(p.getMeeple3()));
			p.setMeeple4(this.resetMeeple(p.getMeeple4()));
			p.setTurn(false);
			if (turn == meeple)
				p.setTurn(true);
			players[meeple - 1] = p;
			meeple++;
		}
		gameState.setRedPlayer(players[0]);
		gameState.setBluePlayer(players[1]);
		gameState.setGreenPlayer(players[2]);
		gameState.setYellowPlayer(players[3]);
		gameState.setExtraTurn(false);
		return gameState;
	}

	private Meeple resetMeeple(Meeple meeple) {
		meeple.setPosition(0);
		meeple.setRelativePosition(0);
		return meeple;
	}

	@Override
	public boolean isKeyFromGame(GameState gameState, String key) {
		if (gameState.getRedPlayer().getKey().equals(key))
			return true;
		if (gameState.getBluePlayer().getKey().equals(key))
			return true;
		if (gameState.getGreenPlayer().getKey().equals(key))
			return true;
		if (gameState.getYellowPlayer().getKey().equals(key))
			return true;
		return false;
	}

	@Override
	public String getPlayerToRoll(GameState gameState) {
		if (gameState.getRedPlayer().getTurn())
			return "red";
		if (gameState.getBluePlayer().getTurn())
			return "blue";
		if (gameState.getGreenPlayer().getTurn())
			return "green";
		if (gameState.getYellowPlayer().getTurn())
			return "yellow";
		return null;
	}

}
