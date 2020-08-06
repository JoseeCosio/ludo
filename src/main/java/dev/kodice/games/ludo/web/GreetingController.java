package dev.kodice.games.ludo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import dev.kodice.games.ludo.TurnExecutor;
import dev.kodice.games.ludo.domain.model.GameSnapshot;
import dev.kodice.games.ludo.domain.model.Greeting;
import dev.kodice.games.ludo.domain.model.HelloMessage;
import dev.kodice.games.ludo.service.GameService;

@Controller
public class GreetingController {

	@Autowired
	GameService gameService;

	@Autowired
	TurnExecutor turn;

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
		if (message.getName().equals("pasame el juego")) {
			List<GameSnapshot> snapshot = gameService.getSnapshot(1L);
			return new Greeting(snapshot.toString());
		}

		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
	}

}