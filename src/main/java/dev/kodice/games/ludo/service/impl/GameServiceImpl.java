package dev.kodice.games.ludo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.kodice.games.ludo.domain.model.Game;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.Meeple;
import dev.kodice.games.ludo.domain.model.Player;
import dev.kodice.games.ludo.repository.GameRepository;
import dev.kodice.games.ludo.repository.GameSnapshotRepository;
import dev.kodice.games.ludo.repository.MeepleRepository;
import dev.kodice.games.ludo.repository.PlayerRepository;
import dev.kodice.games.ludo.service.GameService;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	GameRepository gameRepository;

	@Autowired
	GameSnapshotRepository gameSnapshotRepository;
	
	@Autowired
	MeepleRepository meepleRepository;
	
	@Autowired
	PlayerRepository playerRepository;

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
		game.setRoll(true);
		game.setRolled(0);
		game.setPlayers(this.resetPlayers(game.getPlayers()));
		return game;
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
	public void setDice(int dice, Long gameId) {
		gameRepository.setRolled(dice, gameId);
	}

	@Override
	public void setRoll(Long gameId) {
		gameRepository.setRoll(gameId);
	}

	@Override
	public void setMove(Long gameId) {
		gameRepository.setMove(gameId);
	}

	@Override
	public void updateMeeple(Meeple meeple) {
		meepleRepository.save(meeple);
	}

	@Override
	public List<Game> getGames() {
		return gameRepository.findAll();
	}

	@Override
	public List<Player> getGamePlayers(Long gameId) {
		return playerRepository.findByGameId(gameId);
	}

}
