package dev.kodice.games.ludo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.kodice.games.ludo.domain.model.Game;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.GameState;
import dev.kodice.games.ludo.domain.model.Meeple;
import dev.kodice.games.ludo.domain.model.Player;
import dev.kodice.games.ludo.repository.GameRepository;
import dev.kodice.games.ludo.repository.GameSnapshotRepository;
import dev.kodice.games.ludo.service.GameService;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	GameSnapshotRepository gameSnapshotRepository;

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
	public Game reset(Game game) {
		game.setGameState(this.resetGameState(game.getGameState()));
		return game;
	}

	private GameState resetGameState(GameState gameState) {
		gameState.setExtraTurn(false);
		gameState.setRoll(true);
		gameState.setRolled(0);
		gameState.setMoving(false);
		gameState.setPlayers(this.resetPlayers(gameState.getPlayers()));
		return gameState;
	}

	private List<Player> resetPlayers(List<Player> players) {
		Random ran = new Random();
		int turn = ran.nextInt(players.size()) + 1;
		int num = 1;
		for (Player p : players) {
			p.setTurn(false);
			if (turn == num) {
				p.setTurn(true);
			}
			p.setMeeples(this.resetMeeples(p.getMeeples()));
			num++;
		}
		return players;
	}

	private List<Meeple> resetMeeples(List<Meeple> meeples) {
		for (Meeple m : meeples) {
			m = this.resetMeeple(m);
		}
		return meeples;
	}

	private Meeple resetMeeple(Meeple meeple) {
		meeple.setPosition(0);
		meeple.setRelativePosition(0);
		return meeple;
	}

	@Override
	public boolean isKeyFromGame(List<Player> players, String key) {
		for (Player p : players) {
			if (p.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getPlayerToRoll(List<Player> players) {
		int num = 1;
		for (Player p : players) {
			if (p.getTurn()) {
				return num;
			}
			num++;
		}
		return num;
	}

	@Override
	public List<GameSnapshot> getSnapshot(Long gameId) {
		return gameSnapshotRepository.findAllBygId(gameId);
	}

	@Override
	public void setExtraTurn(Long gameId) {
		gameRepository.setExtraTurn(gameId);
	}

	@Override
	public void removeExtraTurn(Long gameId) {
		gameRepository.removeExtraTurn(gameId);
	}

}
