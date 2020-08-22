package dev.kodice.games.ludo.web;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;

import dev.kodice.games.ludo.SnapExecutor;
import dev.kodice.games.ludo.domain.dto.TurnDto;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.HelloMessage;
import dev.kodice.games.ludo.service.GameService;
import dev.kodice.games.ludo.service.PlayerActionService;

@Controller
public class GreetingController {

	@Autowired
	GameService gameService;

	@Autowired
	SnapExecutor snap;

	@Autowired
	PlayerActionService playerActionService;

	@Transactional
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public TurnDto greeting(HelloMessage message, @RequestHeader String key) throws Exception {
		TurnDto turn = new TurnDto();
		if (message.getName().startsWith("sync")) {
			List<GameSnapshot> snapshot = gameService.getSnapshot(Long.parseLong(message.getName().substring(5)));
			if (snap.isKeyFromGame(key, snapshot)) {
				turn = playerActionService.getRequiredAction(snapshot, key);
				turn.setGame(snap.snap2playerDto(snapshot));
			}
			turn.setMessage("Not valid key mate");
			return turn;
		}
		if (message.getName().startsWith("roll")) {
			Long id = Long.parseLong(message.getName().substring(5));
			List<GameSnapshot> snapshot = gameService.getSnapshot(id);
			if (snap.isKeyFromGame(key, snapshot)) {
				turn = playerActionService.rollDice(id, snapshot, key);
				turn.setGame(snap.snap2playerDto(snapshot));
				return turn;
			}
			turn.setMessage("Not valid key mate");
			return turn;
		}
		if (message.getName().startsWith("move")) {
			Long id = Long.parseLong(message.getName().substring(7));
			List<GameSnapshot> snapshot = gameService.getSnapshot(id);
			if (snap.isKeyFromGame(key, snapshot)) {
				turn = playerActionService.moveMeeple(id, snapshot, key,
						Integer.parseInt(message.getName().substring(5, 6)));
				turn.setGame(snap.snap2playerDto(snapshot));
			}
			turn.setMessage("Not valid key mate");
			return turn;
		}
		turn.setMessage("Syntaxis error, check greetings manual to KodiceLudo at dev.kodice/games/ludo.js");
		return turn;
	}

}