package dev.kodice.games.ludo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import dev.kodice.games.ludo.SnapExecutor;
import dev.kodice.games.ludo.TurnExecutor;
import dev.kodice.games.ludo.domain.dto.MovedMeeple;
import dev.kodice.games.ludo.domain.dto.PlayerActionDto;
import dev.kodice.games.ludo.domain.dto.TurnDto;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.Player;

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
			return this.getActionRequired(snapshot, key);
		}
		if (action.isRoll()) {
			return this.rollDice(id, snapshot, key);
		}
		if (action.getMove() > 0) {
			return this.moveMeeple(id, snapshot, key, action.getMove());
		}
		return turn;
	}
	
	public TurnDto getTurnNoKey(Long id, PlayerActionDto action) {
		List<GameSnapshot> snapshot = gameService.getSnapshot(id);
		TurnDto turn = new TurnDto();
		if (action.isSincronize()) {
			return this.getActionRequiredNoKey(snapshot);
		}
		if (action.isRoll()) {
			return this.rollDiceNoKey(id, snapshot);
		}
		if (action.getMove() > 0) {
			return this.moveMeepleNoKey(id, snapshot, action.getMove());
		}
		return turn;
	}

	private TurnDto getActionRequired(List<GameSnapshot> snapshot, String key) {
		TurnDto turn = new TurnDto();
		Player playerInTurn = snapExecutor.getPlayerInTurn(snapshot);
		if (snapExecutor.isKeyFromGame(key, snapshot)) {
			turn.setPlayerInTurn(snapExecutor.getPlayerToRoll(snapshot));
			if (snapshot.get(0).isSRoll()) {
				turn.setMessage("Waiting " + turn.getPlayerInTurn() + " player to roll!");
				return turn;
			}
			if (snapshot.get(0).isSMove()) {
				turn.setRolled(snapshot.get(0).getSRolled());
				turn.setMoves(turnExecutor.getLegalMoves(playerInTurn, snapshot.get(0).getSRolled()));
				turn.setMessage("Waiting " + turn.getPlayerInTurn() + " player to choose a move!");
				return turn;
			}
		} else {
			turn.setMessage("Not playing that game!");
			return turn;
		}
		return turn;
	}

	private TurnDto rollDice(Long id, List<GameSnapshot> snapshot, String key) {
		TurnDto turn = new TurnDto();
		Player playerInTurn = snapExecutor.getPlayerInTurn(snapshot);
		if (playerInTurn.getKey().equals(key)) {
			if (snapshot.get(0).isSRoll()) {
				int dice = turnExecutor.rollDice();
				if (dice == 6) {
					gameService.setExtraTurn(id);
				}
				List<Boolean> legalMoves = new ArrayList<Boolean>();
				legalMoves = turnExecutor.getLegalMoves(playerInTurn, dice);
				turn.setMoves(legalMoves);
				int moves = turnExecutor.getNumberOfLegalMoves(legalMoves);
				turn.setRolled(dice);
				if (moves == 0) {
					gameService.removeExtraTurn(id);
					snapExecutor.passTurn(snapshot);
					return turn;
				}
				if (moves == 1) {
					turn.setMovedMeeples(snapExecutor.moveMeeple(snapshot, turnExecutor.getLegalMove(legalMoves)));
					if (!gameService.getGameById(id).get().getGameState().isExtraTurn()) {
						snapExecutor.passTurn(snapshot);
					}
					gameService.removeExtraTurn(id);
					return turn;
				}
				gameService.setRolled(dice, id);
				if (moves > 1) {
					gameService.setMove(id);
					return turn;
				}
			}
			if (!gameService.getGameById(id).get().getGameState().isMoving()) {
				turn.setMessage("Waitign for a move, not a roll!");
				turn.setRolled(snapshot.get(0).getSRolled());
				turn.setMoves(turnExecutor.getLegalMoves(playerInTurn, turn.getRolled()));
				return turn;
			}
		} else {
			turn.setMessage("Not that player!");
			return turn;
		}
		return turn;
	}

	private TurnDto moveMeeple(Long id, List<GameSnapshot> snapshot, String key, int moving) {
		TurnDto turn = new TurnDto();
		Player playerInTurn = snapExecutor.getPlayerInTurn(snapshot);
		if (playerInTurn.getKey().equals(key)) {
			if (snapshot.get(0).isSMove()) {
				List<MovedMeeple> movedMeeples = snapExecutor.moveMeeple(snapshot, moving);
				turn.setMovedMeeples(movedMeeples);
				gameService.setRoll(id);
				if (!gameService.getGameById(id).get().getGameState().isExtraTurn()) {
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

	public TurnDto getActionRequiredNoKey(List<GameSnapshot> snapshot) {
		TurnDto turn = new TurnDto();
		Player playerInTurn = snapExecutor.getPlayerInTurn(snapshot);
		turn.setPlayerInTurn(snapExecutor.getPlayerToRoll(snapshot));
		if (snapshot.get(0).isSRoll()) {
			turn.setMessage("Waiting " + turn.getPlayerInTurn() + " player to roll!");
			return turn;
		}
		if (snapshot.get(0).isSMove()) {
			turn.setRolled(snapshot.get(0).getSRolled());
			turn.setMoves(turnExecutor.getLegalMoves(playerInTurn, snapshot.get(0).getSRolled()));
			turn.setMessage("Waiting " + turn.getPlayerInTurn() + " player to choose a move!");
			return turn;
		}
		return turn;
	}

	public TurnDto rollDiceNoKey(Long id, List<GameSnapshot> snapshot) {
		TurnDto turn = new TurnDto();
		Player playerInTurn = snapExecutor.getPlayerInTurn(snapshot);
		if (snapshot.get(0).isSRoll()) {
			int dice = turnExecutor.rollDice();
			if (dice == 6) {
				gameService.setExtraTurn(id);
			}
			List<Boolean> legalMoves = new ArrayList<Boolean>();
			legalMoves = turnExecutor.getLegalMoves(playerInTurn, dice);
			turn.setMoves(legalMoves);
			int moves = turnExecutor.getNumberOfLegalMoves(legalMoves);
			turn.setRolled(dice);
			if (moves == 0) {
				gameService.removeExtraTurn(id);
				snapExecutor.passTurn(snapshot);
				return turn;
			}
			if (moves == 1) {
				turn.setMovedMeeples(snapExecutor.moveMeeple(snapshot, turnExecutor.getLegalMove(legalMoves)));
				if (!gameService.getGameById(id).get().getGameState().isExtraTurn()) {
					snapExecutor.passTurn(snapshot);
				}
				gameService.removeExtraTurn(id);
				return turn;
			}
			gameService.setRolled(dice, id);
			if (moves > 1) {
				gameService.setMove(id);
				return turn;
			}
		}
		if (!gameService.getGameById(id).get().getGameState().isMoving()) {
			turn.setMessage("Waitign for a move, not a roll!");
			turn.setRolled(snapshot.get(0).getSRolled());
			turn.setMoves(turnExecutor.getLegalMoves(playerInTurn, turn.getRolled()));
			return turn;
		}
		return turn;
	}

	public TurnDto moveMeepleNoKey(Long id, List<GameSnapshot> snapshot, int moving) {
		TurnDto turn = new TurnDto();
			if (snapshot.get(0).isSMove()) {
				List<MovedMeeple> movedMeeples = snapExecutor.moveMeeple(snapshot, moving);
				turn.setMovedMeeples(movedMeeples);
				gameService.setRoll(id);
				if (!gameService.getGameById(id).get().getGameState().isExtraTurn()) {
					snapExecutor.passTurn(snapshot);
				}
				gameService.removeExtraTurn(id);
				turn.setPlayerInTurn(snapExecutor.getPlayerToRoll(snapshot));
				return turn;
			} else {
				turn.setMessage("Waiting for a roll, not a move!");
				return turn;
			}
	}

}
