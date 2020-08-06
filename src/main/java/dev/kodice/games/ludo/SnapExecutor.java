package dev.kodice.games.ludo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.kodice.games.ludo.domain.dto.GameStateDto;
import dev.kodice.games.ludo.domain.dto.Landing;
import dev.kodice.games.ludo.domain.dto.MovedMeeple;
import dev.kodice.games.ludo.domain.dto.PlayerDto;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.GameState;
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

	public Player getPlayerInTurn(List<GameSnapshot> snapshotGame) {
		Player player = new Player();
		List<Meeple> meeples = new ArrayList<Meeple>();
		for (GameSnapshot game : snapshotGame) {
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
		GameState game = this.snapshotToGameState(snapshot);
		System.out.println(game);
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
					MovedMeeple movedMeeple = new MovedMeeple(game.getPlayers().get(turn - 1).getId(), moving,
							m.getPosition() - game.getRolled(), m.getPosition());
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

	public GameState snapshotToGameState(List<GameSnapshot> snapshot) {
		List<Player> players = new ArrayList<Player>();
		List<Meeple> meeples = new ArrayList<Meeple>();
		int index=0;
		for(GameSnapshot g:snapshot) {
			if(index%4==0) {
				players.add(new Player(g.getPId(),g.getPKey(),g.isPTurn()));
			}
			meeples.add(new Meeple(g.getMId(),g.getMPos(),g.getMRel()));
			if(index%4==3) {
				players.get(index/4).setMeeples(meeples);
				meeples = new ArrayList<Meeple>();
			}
			index++;
		}
		GameState game = new GameState();
		game.setId(snapshot.get(0).getGId());
		game.setPlayers(players);
		game.setRolled(snapshot.get(0).getSRolled());
		return game;
	}

	public GameStateDto snapshotToGameStateDto(List<GameSnapshot> snapshot) {
		List<PlayerDto> players = new ArrayList<PlayerDto>();
		List<Long> meeples = new ArrayList<Long>();
		int index=0;
		for(GameSnapshot g:snapshot) {
			if(index%4==0) {
				players.add(new PlayerDto(g.getPId()));
			}
			meeples.add((long) g.getMPos());
			if(index%4==3) {
				players.get(index/4).setMeeples(meeples);
				meeples = new ArrayList<Long>();
			}
			index++;
		}
		GameStateDto game = new GameStateDto();
		game.setId(snapshot.get(0).getGId());
		game.setPlayers(players);
		return game;
	}

}
