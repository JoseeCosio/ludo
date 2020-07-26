package dev.kodice.games.ludo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import dev.kodice.games.ludo.domain.model.Game;
//import dev.kodice.games.ludo.domain.model.GameState;
//import dev.kodice.games.ludo.domain.model.Player;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

//		TurnExecutor turn = new TurnExecutor();
//		int dice;
//		Game game = new Game();
//		System.out.println(game);
//		GameState gameState = new GameState();
//		Player player;
//		MovingMeeples movingMeeples;
//		int turno = 0;
//		int victoria = 0;
//		while (victoria == 0) {
//			turno++;
//			System.out.println("Turno numero " + turno);
//			player = turn.getPlayerInTurn(game.getGameState());
//			dice = turn.rollDice();
//			System.out.println("El dado rueda en un " + dice);
//			if(dice==6) {
//				gameState = game.getGameState();
//				gameState.setExtraTurn(true);
//				game.setGameState(gameState);
//				System.out.println("Obtienes otra tirada!");
//			}
//			movingMeeples = turn.getLegalMoves(player, dice);
//			System.out.println("El jugador tiene " + movingMeeples + " movimientos posibles!");
//			int meepleToMove;
//			if (movingMeeples.getMoving() > 0) {
//				meepleToMove = turn.getMeepleToMove(movingMeeples.getMoving());
//				System.out.println("El jugador elige la opcion numero " + meepleToMove);
//				game.setGameState(turn.moveMeeple(game.getGameState(), dice, meepleToMove));
//				System.out.println(game);
//				if (!game.getGameState().isExtraTurn()) {
//					game.setGameState(turn.passTurn(game.getGameState()));
//				}
//			} else {
//				game.setGameState(turn.passTurn(game.getGameState()));
//			}
//			System.out.println();
//			if (player.getMeeple1().getRelativePosition() == 57 && player.getMeeple2().getRelativePosition() == 57
//					&& player.getMeeple3().getRelativePosition() == 57
//					&& player.getMeeple4().getRelativePosition() == 57) {
//				victoria = 1;
//				System.out.println("El jugador ha llevado todas sus fichas a la meta!");
//			}
//			gameState = game.getGameState();
//			gameState.setExtraTurn(false);
//			game.setGameState(gameState);
//		}
//
	}
}