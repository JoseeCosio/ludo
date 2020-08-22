package dev.kodice.games.ludo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.kodice.games.ludo.SnapExecutor;
import dev.kodice.games.ludo.TurnExecutor;
import dev.kodice.games.ludo.domain.dto.MovedMeeple;
import dev.kodice.games.ludo.domain.dto.PlayerActionDto;
import dev.kodice.games.ludo.domain.dto.TurnDto;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.Player;

@Service
public class PlayerActionService {

	@Autowired
	GameService gameService;

	@Autowired
	SnapExecutor snapExecutor;

	@Autowired
	TurnExecutor turnExecutor;

	public TurnDto getTurn(Long id, PlayerActionDto action, String key) {
		List<GameSnapshot> snapshot = gameService.getSnapshot(id);
		TurnDto turn = new TurnDto();
		if (action.isSincronize()) {
			return this.getRequiredAction(snapshot, key);
		}
		if (action.isRoll()) {
			return this.rollDice(id, snapshot, key);
		}
		if (action.getMove() > 0) {
			return this.moveMeeple(id, snapshot, key, action.getMove());
		}
		return turn;
	}

	public TurnDto getRequiredAction(List<GameSnapshot> snapshot, String key) {
		TurnDto turn = new TurnDto();
		turn.setPlayerInTurn(snapExecutor.getPlayerToRoll(snapshot));
		if (snapshot.get(0).isSRoll()) {
			turn.setMessage("Waiting " + turn.getPlayerInTurn() + " player to roll!");
			return turn;
		}
		if (!snapshot.get(0).isSRoll()) {
			turn.setDice(snapshot.get(0).getSRolled());
			turn.setMoves(turnExecutor.getLegalMoves(snapExecutor.getPlayerInTurn(snapshot).getMeeples(),
					snapshot.get(0).getSRolled()));
			turn.setMessage("Waiting " + turn.getPlayerInTurn() + " player to choose a move!");
			return turn;
		}
		return turn;
	}

	public TurnDto rollDice(Long id, List<GameSnapshot> snapshot, String key) {
		TurnDto turn = new TurnDto();
		if (snapExecutor.getPlayerInTurn(snapshot).getKey().equals(key)) {
			if (snapshot.get(0).isSRoll()) {
				int dice = turnExecutor.rollDice();
				if (dice == 6) {
					gameService.setExtraTurn(id);
				}
				List<Boolean> legalMoves = new ArrayList<Boolean>();
				legalMoves = turnExecutor.getLegalMoves(snapExecutor.getPlayerInTurn(snapshot).getMeeples(), dice);
				turn.setMoves(legalMoves);
				int moves = turnExecutor.getNumberOfLegalMoves(legalMoves);
				turn.setDice(dice);
				gameService.setDice(dice, id);
				if (moves == 0) {
					gameService.removeExtraTurn(id);
					snapExecutor.passTurn(snapshot);
					return turn;
				}
				if (moves == 1) {
					turn.setMovedMeeples(snapExecutor.moveMeeple(snapshot, turnExecutor.getLegalMove(legalMoves)));
					if (!gameService.getGameById(id).get().isExtraTurn()) {
						snapExecutor.passTurn(snapshot);
					}
					gameService.removeExtraTurn(id);
					return turn;
				}
				if (moves > 1) {
					gameService.setMove(id);
					return turn;
				}
			}
			if (!gameService.getGameById(id).get().isRoll()) {
				turn.setMessage("Waitign for a move, not a roll!");
				turn.setDice(snapshot.get(0).getSRolled());
				turn.setMoves(turnExecutor.getLegalMoves(snapExecutor.getPlayerInTurn(snapshot).getMeeples(),
						turn.getDice()));
				return turn;
			}
		} else {
			turn.setMessage("Not that player!");
			return turn;
		}
		return turn;
	}

	public TurnDto moveMeeple(Long id, List<GameSnapshot> snapshot, String key, int moving) {
		TurnDto turn = new TurnDto();
		Player playerInTurn = snapExecutor.getPlayerInTurn(snapshot);
		if (playerInTurn.getKey().equals(key)) {
			if (!snapshot.get(0).isSRoll()) {
				List<MovedMeeple> movedMeeples = snapExecutor.moveMeeple(snapshot, moving);
				turn.setMovedMeeples(movedMeeples);
				gameService.setRoll(id);
				if (!gameService.getGameById(id).get().isExtraTurn()) {
					snapExecutor.passTurn(snapshot);
				}
				gameService.removeExtraTurn(id);
				turn.setPlayerInTurn(snapExecutor.getPlayerToRoll(snapshot));
				return turn;
			} else {
				turn.setMessage("Waiting for a roll, not a move!");
				return turn;
			}
		} else {
			turn.setMessage("Not that player!");
			return turn;
		}
	}

}
