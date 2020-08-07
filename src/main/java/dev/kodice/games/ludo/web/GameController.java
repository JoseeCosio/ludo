package dev.kodice.games.ludo.web;

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
import dev.kodice.games.ludo.domain.dto.PlayerActionDto;
import dev.kodice.games.ludo.domain.dto.RegisterDto;
import dev.kodice.games.ludo.domain.dto.TurnDto;
import dev.kodice.games.ludo.domain.model.Game;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.Session;
import dev.kodice.games.ludo.service.GameService;
import dev.kodice.games.ludo.service.PlayerActionService;
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
	SnapExecutor snapExecutor;
	
	@Autowired
	PlayerActionService playerActionService;

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
		return playerActionService.getTurn(id, action, key);
	}
}
