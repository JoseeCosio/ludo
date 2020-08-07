package dev.kodice.games.ludo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import dev.kodice.games.ludo.SnapExecutor;
import dev.kodice.games.ludo.domain.dto.GameStateDto;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.HelloMessage;
import dev.kodice.games.ludo.service.GameService;

@Controller
public class GreetingController {

	@Autowired
	GameService gameService;

	@Autowired
	SnapExecutor snap;

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public GameStateDto greeting(HelloMessage message) throws Exception {
		if (message.getName().equals("pasame el juego")) {
			List<GameSnapshot> snapshot = gameService.getSnapshot(1L);
			GameStateDto game = snap.snapshotToGameStateDto(snapshot);
			return game;
		}
		return null;
	}

}