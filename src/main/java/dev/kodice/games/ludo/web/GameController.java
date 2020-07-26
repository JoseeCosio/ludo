package dev.kodice.games.ludo.web;

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

import dev.kodice.games.ludo.PlayerAndLand;
import dev.kodice.games.ludo.TurnExecutor;
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
		Game game = new Game();
		gameService.newGame(game);
		return game.getId();
	}

	@GetMapping("/reset/{id}")
	public Game resetGame(@PathVariable Long id) {
		Game game = gameService.getGameById(id).get();
		game.setGameState(gameService.reset(game.getGameState()));
		return gameService.update(game);
	}

	@PostMapping("/register")
	public Session registerUser(@RequestBody RegisterDto registerDto) {
		return sessionService.createSession(registerDto);
	}

	@GetMapping("/{gameId}/getTurn")
	public TurnDto getTurn(@PathVariable Long gameId, @RequestBody PlayerActionDto action, @RequestHeader String key) {
		Game game = gameService.getGameById(gameId).get();
		GameState gameState = game.getGameState();
		TurnDto turn = new TurnDto();
		Player playerInTurn = turnExecutor.getPlayerInTurn(gameState);
		if (action.isSincronize()) {
			if (gameService.isKeyFromGame(gameState, key)) {
				turn.setRoll(gameService.getPlayerToRoll(gameState));
				if (game.getGameState().isRoll()) {
					turn.setMessage("Waiting " + turn.getRoll() + " player to roll!");
					return turn;
				}
				if (game.getGameState().isMoving()) {
					turn.setRolled(gameState.getRolled());
					turn.setMoves(turnExecutor.getLegalMoves(playerInTurn, game.getGameState().getRolled()));
					turn.setMessage("Waiting " + turn.getRoll() + " player to choose a move!");
					return turn;
				}
			} else {
				turn.setMessage("Not playing that game!");
				return turn;
			}
		}
		if (action.isRoll()) {
			if (gameService.isKeyFromPlayer(playerInTurn, key)) {
				if (game.getGameState().isRoll()) {
					int dice;
					dice = turnExecutor.rollDice();
					System.out.println(gameService.getPlayerToRoll(gameState) + " player rolled a " + dice);
					if (dice == 6) {
						gameState.setExtraTurn(true);
					}
					gameState.setRolled(dice);
					turn.setRolled(dice);
					turn.setMoves(turnExecutor.getLegalMoves(playerInTurn, dice));
					if (turn.getMoves().getMoving() == 0) {
						System.out.println("No tienes movimientos posibles!");
						gameState.setExtraTurn(false);
						gameState = turnExecutor.passTurn(gameState);
						System.out.println(gameState);
						game.setGameState(gameState);
						gameService.save(game);
						return turn;
					}
					if (turn.getMoves().getMoving() == 1) {
						System.out.println("Movimiento único!");
						PlayerAndLand playLand = turnExecutor.moveMeeple(gameState, dice, 1);
						gameState = playLand.getGameState();
						turn.setMovedMeeples(playLand.getMovedMeeples());
						if (!gameState.isExtraTurn()) {
							gameState = turnExecutor.passTurn(gameState);
						}
						gameState.setExtraTurn(false);
						System.out.println(gameState);
						game.setGameState(gameState);
						gameService.save(game);
						return turn;
					}
					if (turn.getMoves().getMoving() > 1) {
						System.out.println("Más de 1 movimiento posible, enviar decisión!");
						gameState.setRoll(false);
						gameState.setMoving(true);
						System.out.println(gameState);
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
			if (gameService.isKeyFromPlayer(playerInTurn, key)) {
				if (game.getGameState().isMoving()) {
					System.out.println(
							gameService.getPlayerToRoll(gameState) + " choose option number " + action.getMove() + ".");
					PlayerAndLand playLand = turnExecutor.moveMeeple(gameState, gameState.getRolled(), action.getMove());
					gameState = playLand.getGameState();
					turn.setMovedMeeples(playLand.getMovedMeeples());
					gameState.setMoving(false);
					gameState.setRoll(true);
					if (!gameState.isExtraTurn()) {
						gameState = turnExecutor.passTurn(game.getGameState());
					}
					gameState.setExtraTurn(false);
					System.out.println(gameState);
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
