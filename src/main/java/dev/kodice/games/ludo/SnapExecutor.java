package dev.kodice.games.ludo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.kodice.games.ludo.domain.dto.Landing;
import dev.kodice.games.ludo.domain.dto.MovedMeeple;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.Meeple;
import dev.kodice.games.ludo.domain.model.Player;
import dev.kodice.games.ludo.repository.PlayerRepository;

@Service
public class SnapExecutor {

	@Autowired
	PlayerRepository playerRepository;

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
		playerRepository.setTurn(index.get((turn+1)%4));
	}

	public List<MovedMeeple> moveMeeple(int dice, int moving) {
		List<MovedMeeple> moved = new ArrayList<MovedMeeple>();
		int turn = 1;
		TurnExecutor turnExe = new TurnExecutor();
		for (Player p : this.getPlayers()) {
			if (p.getTurn()) {
				Meeple m = this.getMeeple(turn, moving);
				Landing landing = turnExe.getLandingCell(m, dice, turn);
				if (turnExe.canMeepleMove(m, dice)) {
					this.updateMeeple(turn, moving, landing);
					MovedMeeple movedMeeple = new MovedMeeple(this.getPlayers().get(turn - 1).getId(), moving,
							m.getPosition() - dice, m.getPosition());
					moved.add(movedMeeple);
				}
				if (!turnExe.isCellProtected(landing.getPosition())) {
					List<MovedMeeple> movedMeeples = this.kickMeeples(landing.getPosition(), turn);
					if (movedMeeples.size() > 0) {
						moved.addAll(movedMeeples);
					}
				}
				if (landing.getPosition() == 57) {
					this.setExtraTurn(true);
				}
			}
			turn++;
		}
		return moved;
	}
}
