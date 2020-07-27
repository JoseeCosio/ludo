package dev.kodice.games.ludo.web;

import java.util.ArrayList;
import java.util.List;

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

import dev.kodice.games.ludo.TurnExecutor;
import dev.kodice.games.ludo.domain.dto.GameStateDto;
import dev.kodice.games.ludo.domain.dto.MovedMeeple;
import dev.kodice.games.ludo.domain.dto.PlayerActionDto;
import dev.kodice.games.ludo.domain.dto.RegisterDto;
import dev.kodice.games.ludo.domain.dto.TurnDto;
import dev.kodice.games.ludo.domain.model.Game;
import dev.kodice.games.ludo.domain.model.GameState;
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
	public GameStateDto reconnect(@PathVariable Long id, @RequestHeader String key) {
		Game game = gameService.getGameById(id).get();
		if (gameService.isKeyFromGame(game.getGameState().getPlayers(), key)) {
			GameStateDto gameStateDto = turnExecutor.gameStateToGameStateDto(game.getGameState());
			return gameStateDto;
		}
		return null;
	}

	@GetMapping("/{gameId}/getTurn")
	public TurnDto getTurn(@PathVariable Long gameId, @RequestBody PlayerActionDto action, @RequestHeader String key) {
		Game game = gameService.getGameById(gameId).get();
		GameState gameState = game.getGameState();
		TurnDto turn = new TurnDto();
		Player playerInTurn = turnExecutor.getPlayerInTurn(gameState.getPlayers());
		if (action.isSincronize()) {
			if (gameService.isKeyFromGame(gameState.getPlayers(), key)) {
				turn.setPlayerInTurn(gameService.getPlayerToRoll(gameState.getPlayers()));
				if (game.getGameState().isRoll()) {
					turn.setMessage("Waiting " + turn.getPlayerInTurn() + " player to roll!");
					return turn;
				}
				if (game.getGameState().isMoving()) {
					turn.setRolled(gameState.getRolled());
					turn.setMoves(turnExecutor.getLegalMoves(playerInTurn, game.getGameState().getRolled()));
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
				if (gameState.isRoll()) {
					int dice = turnExecutor.rollDice();
					if (dice == 6) {
						gameState.setExtraTurn(true);
					}
					List<Boolean> legalMoves = new ArrayList<Boolean>();
					legalMoves = turnExecutor.getLegalMoves(playerInTurn, dice);
					turn.setMoves(legalMoves);
					int moves = turnExecutor.getNumberOfLegalMoves(legalMoves);
					turn.setRolled(dice);
					if (moves == 0) {
						gameState.setExtraTurn(false);
						gameState.setPlayers(turnExecutor.passTurn(gameState.getPlayers()));
						game.setGameState(gameState);
						gameService.save(game);
						return turn;
					}
					if (moves == 1) {
						turn.setMovedMeeples(gameState.moveMeeple(dice, turnExecutor.getLegalMove(legalMoves)));
						if (!gameState.isExtraTurn()) {
							gameState.setPlayers(turnExecutor.passTurn(gameState.getPlayers()));
						}
						gameState.setExtraTurn(false);
						game.setGameState(gameState);
						gameService.save(game);
						return turn;
					}
					gameState.setRolled(dice);
					if (moves > 1) {
						gameState.setRoll(false);
						gameState.setMoving(true);
						game.setGameState(gameState);
						gameService.save(game);
						return turn;
					}
				}
				if (game.getGameState().isMoving()) {
					turn.setMessage("Waitign for a move, not a roll!");
					turn.setRolled(gameState.getRolled());
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
				if (game.getGameState().isMoving()) {
					List<MovedMeeple> movedMeeples = gameState.moveMeeple(gameState.getRolled(), action.getMove());
					turn.setMovedMeeples(movedMeeples);
					gameState.setMoving(false);
					gameState.setRoll(true);
					if (!gameState.isExtraTurn()) {
						gameState.setPlayers(turnExecutor.passTurn(gameState.getPlayers()));
					}
					gameState.setExtraTurn(false);
					turn.setPlayerInTurn(gameService.getPlayerToRoll(gameState.getPlayers()));
					game.setGameState(gameState);
					gameService.save(game);
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
