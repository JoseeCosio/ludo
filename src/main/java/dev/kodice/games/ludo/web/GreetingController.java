package dev.kodice.games.ludo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import dev.kodice.games.ludo.TurnExecutor;
import dev.kodice.games.ludo.domain.dto.GameStateDto;
import dev.kodice.games.ludo.domain.model.Game;
import dev.kodice.games.ludo.domain.model.Greeting;
import dev.kodice.games.ludo.domain.model.HelloMessage;
import dev.kodice.games.ludo.repository.GameRepository;

@Controller
public class GreetingController {

	@Autowired
	GameRepository gameRepository;

	@Autowired
	TurnExecutor turn;

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
		if (message.getName().equals("pasame el juego")) {
			Game game = gameRepository.getOne(1L);
			GameStateDto gameStateDto = turn.gameStateToGameStateDto(game.getGameState());
			System.out.println(gameStateDto.toString());
			return new Greeting(gameStateDto.toString());
		}

		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
	}

}