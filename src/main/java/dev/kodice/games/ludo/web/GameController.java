package dev.kodice.games.ludo.web;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.kodice.games.ludo.SnapExecutor;
import dev.kodice.games.ludo.TurnExecutor;
import dev.kodice.games.ludo.domain.dto.MovedMeeple;
import dev.kodice.games.ludo.domain.dto.PlayerActionDto;
import dev.kodice.games.ludo.domain.dto.RegisterDto;
import dev.kodice.games.ludo.domain.dto.TurnDto;
import dev.kodice.games.ludo.domain.model.Game;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.Player;
import dev.kodice.games.ludo.domain.model.Session;
import dev.kodice.games.ludo.service.GameService;
import dev.kodice.games.ludo.service.SessionService;

@RestController
@RequestMapping(path = "/games", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@CrossOrigin
public class GameController {

	@Autowired
	GameService gameService;

	@Autowired
	SessionService sessionService;

	@Autowired
	TurnExecutor turnExecutor;

	@Autowired
	SnapExecutor snapExecutor;

	@GetMapping("/newGame")
	@ResponseStatus(code = HttpStatus.CREATED)
	public Long newGame() {
		Game game = new Game(4);
		gameService.newGame(game);
		return game.getId();
	}

	@GetMapping("/reset/{id}")
	public Game resetGame(@PathVariable Long id) {
		Game game = gameService.getGameById(id).get();
		game = gameService.reset(game);
		return gameService.update(game);
	}

	@PostMapping("/register")
	public Session registerUser(@RequestBody RegisterDto registerDto) {
		return sessionService.createSession(registerDto);
	}

	@GetMapping("/reconnect/{id}")
	public List<GameSnapshot> reconnect(@PathVariable Long id, @RequestHeader String key) {
		List<GameSnapshot> snapshot = gameService.getSnapshot(id);
		if (snapExecutor.isKeyFromGame(key, snapshot)) {
			return snapshot;
		}
		return null;
	}

	@Transactional
	@GetMapping("/{id}/getTurn")
	public TurnDto getTurn(@PathVariable Long id, @RequestBody PlayerActionDto action, @RequestHeader String key) {
		List<GameSnapshot> snapshot = gameService.getSnapshot(id);
		Player playerInTurn = snapExecutor.getPlayerInTurn(snapshot);
		TurnDto turn = new TurnDto();
		if (action.isSincronize()) {
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
		}
		if (action.isRoll()) {
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
					gameService.setRolled(dice,id);
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

		}
		if (action.getMove() > 0) {
			if (playerInTurn.getKey().equals(key)) {
				if (snapshot.get(0).isSMove()) {
					List<MovedMeeple> movedMeeples = snapExecutor.moveMeeple(snapshot, action.getMove());
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
		return turn;
	}
}
