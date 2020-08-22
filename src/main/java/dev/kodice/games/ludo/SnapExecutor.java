package dev.kodice.games.ludo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.kodice.games.ludo.domain.dto.GameStateDto;
import dev.kodice.games.ludo.domain.dto.Landing;
import dev.kodice.games.ludo.domain.dto.MovedMeeple;
import dev.kodice.games.ludo.domain.dto.PlayerDto;
import dev.kodice.games.ludo.domain.dto.front.FrontPlayerDto;
import dev.kodice.games.ludo.domain.dto.front.GameDto;
import dev.kodice.games.ludo.domain.model.FrontSnapshot;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.Game;
import dev.kodice.games.ludo.domain.model.Meeple;
import dev.kodice.games.ludo.domain.model.Player;
import dev.kodice.games.ludo.repository.PlayerRepository;
import dev.kodice.games.ludo.service.GameService;

@Service
public class SnapExecutor {

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	GameService gameService;

	public Player getPlayerInTurn(List<GameSnapshot> snapshot) {
		Player player = new Player();
		List<Meeple> meeples = new ArrayList<Meeple>();
		for (GameSnapshot game : snapshot) {
			if (game.isPTurn()) {
				if (!player.getTurn()) {
					player.setId(game.getPId());
					player.setKey(game.getPKey());
					player.setTurn(true);
				}
				Meeple meeple = new Meeple();
				meeple.setId(game.getMId());
				meeple.setPosition(game.getMPos());
				meeple.setRelativePosition(game.getMRel());
				meeples.add(meeple);
			}
		}
		player.setMeeples(meeples);
		return player;
	}

	public boolean isKeyFromGame(String key, List<GameSnapshot> snapshotGame) {
		for (GameSnapshot g : snapshotGame) {
			if (g.getPKey().equals(key))
				return true;
		}
		return false;
	}

	public int getPlayerToRoll(List<GameSnapshot> snapshotGame) {
		Long player = 0L;
		int num = 0;
		for (GameSnapshot g : snapshotGame) {
			if (!g.getPId().equals(player)) {
				player = g.getPId();
				num++;
				if (g.isPTurn()) {
					return num;
				}
			}
		}
		return num;
	}

	public void passTurn(List<GameSnapshot> snapshotGame) {
		Long player = 0L;
		int num = 0;
		int turn = 0;
		List<Long> index = new ArrayList<Long>();
		for (GameSnapshot g : snapshotGame) {
			if (!g.getPId().equals(player)) {
				if (g.isPTurn()) {
					turn = num;
				}
				num++;
				player = g.getPId();
				index.add(g.getPId());
			}
		}
		playerRepository.removeTurn(index.get(turn));
		playerRepository.setTurn(index.get((turn + 1) % 4));
	}

	public List<MovedMeeple> moveMeeple(List<GameSnapshot> snapshot, int moving) {
		Game game = this.snapshotToGameState(snapshot);
		return this.move(game, moving);
	}

	public List<MovedMeeple> moveMeeple(List<GameSnapshot> snapshot, int moving, int dice) {
		Game game = this.snapshotToGameState(snapshot, dice);
		return this.move(game, moving);
	}

	private List<MovedMeeple> move(Game game, int moving) {
		List<MovedMeeple> moved = new ArrayList<MovedMeeple>();
		int turn = 1;
		TurnExecutor turnExe = new TurnExecutor();
		for (Player p : game.getPlayers()) {
			if (p.getTurn()) {
				Meeple m = game.getMeeple(turn, moving);
				Landing landing = turnExe.getLandingCell(m, game.getRolled(), turn);
				if (turnExe.canMeepleMove(m, game.getRolled())) {
					game.updateMeeple(turn, moving, landing);
					Meeple m2save = game.getMeeple(turn, moving);
					System.out.println(m2save);
					gameService.updateMeeple(m2save);
					int initial = m.getPosition() - game.getRolled();
					if (initial < 0) {
						initial = 0;
					}
					MovedMeeple movedMeeple = new MovedMeeple(game.getPlayers().get(turn - 1).getId(), moving, initial,
							m.getPosition());
					moved.add(movedMeeple);
				}
				if (!turnExe.isCellProtected(landing.getPosition())) {
					List<MovedMeeple> movedMeeples = game.kickMeeples(landing.getPosition(), turn);
					for (MovedMeeple up : movedMeeples) {
						gameService.updateMeeple(game.getMeeple(up.getPlayerId().intValue(), up.getMeeple()));
					}
					if (movedMeeples.size() > 0) {
						moved.addAll(movedMeeples);
					}
				}
				if (landing.getPosition() == 57) {
					game.setExtraTurn(true);
				}
			}
			turn++;
		}
		return moved;
	}

	public Game snapshotToGameState(List<GameSnapshot> snapshot) {
		Game game = this.snapToGame(snapshot);
		game.setRolled(snapshot.get(0).getSRolled());
		return game;
	}

	public Game snapshotToGameState(List<GameSnapshot> snapshot, int dice) {
		Game game = this.snapToGame(snapshot);
		game.setRolled(dice);
		return game;
	}

	private Game snapToGame(List<GameSnapshot> snapshot) {
		List<Player> players = new ArrayList<Player>();
		List<Meeple> meeples = new ArrayList<Meeple>();
		int index = 0;
		for (GameSnapshot g : snapshot) {
			if (index % 4 == 0) {
				players.add(new Player(g.getPId(), g.getPKey(), g.isPTurn()));
			}
			meeples.add(new Meeple(g.getMId(), g.getMPos(), g.getMRel()));
			if (index % 4 == 3) {
				players.get(index / 4).setMeeples(meeples);
				meeples = new ArrayList<Meeple>();
			}
			index++;
		}
		Game game = new Game();
		game.setId(snapshot.get(0).getGId());
		game.setPlayers(players);
		return game;
	}

	public GameStateDto snapshotToGameStateDto(List<GameSnapshot> snapshot) {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		List<Long> meeples = new ArrayList<Long>();
		int index = 0;
		for (GameSnapshot g : snapshot) {
			if (index % 4 == 0) {
				players.add(new PlayerDto(g.getPId()));
			}
			meeples.add((long) g.getMPos());
			if (index % 4 == 3) {
				players.get(index / 4).setMeeples(meeples);
				meeples = new ArrayList<Long>();
			}
			index++;
		}
		GameStateDto game = new GameStateDto();
		game.setId(snapshot.get(0).getGId());
		game.setPlayers(players);
		return game;
	}

	public List<FrontSnapshot> mapToFrontSnapshot(List<GameSnapshot> snapshot) {
		List<FrontSnapshot> snap = new ArrayList<FrontSnapshot>();
		for (GameSnapshot g : snapshot) {
			snap.add(g.mapToFrontSnapshot());
		}
		return snap;
	}

	public GameDto snap2playerDto(List<GameSnapshot> snapshot) {
		List<FrontPlayerDto> players = new ArrayList<FrontPlayerDto>();
		List<FrontPlayerDto> front = new ArrayList<FrontPlayerDto>();
		List<Long> meeples = new ArrayList<Long>();
		List<Long> meeple = new ArrayList<Long>();
		front.add(new FrontPlayerDto(meeples));
		GameDto gameDto = new GameDto(0,"",0,front);
		int index = 0;
		for (GameSnapshot g : snapshot) {
			if (index % 4 == 0) {
				players.add(new FrontPlayerDto(meeple));
				if (g.isPTurn()) {
					gameDto.setPlayerTurn(index / 4 + 1);
				}
			}
			meeples.add((long) g.getMPos());
			if (index % 4 == 3) {
				players.get(index / 4).setMeeples(meeples);
				meeples = new ArrayList<Long>();
			}
			index++;
		}
		gameDto.setPlayers(players);
		gameDto.setDice(snapshot.get(0).getSRolled());
		if (snapshot.get(0).isSRoll()) {
			gameDto.setRequiredAction("roll");
		} else {
			gameDto.setRequiredAction("move");
		}
		return gameDto;
	}

}
