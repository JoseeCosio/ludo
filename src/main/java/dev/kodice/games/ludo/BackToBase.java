package dev.kodice.games.ludo;

import java.util.ArrayList;
import java.util.List;

import dev.kodice.games.ludo.domain.dto.MovedMeeples;
import dev.kodice.games.ludo.domain.model.GameState;
import dev.kodice.games.ludo.domain.model.Meeple;
import dev.kodice.games.ludo.domain.model.Player;

public class BackToBase {

	public PlayerAndLand kickEnemies(GameState gameState, Meeple meeple, String turn) {
		PlayerAndLand land = new PlayerAndLand();
		List<MovedMeeples> moved = new ArrayList<MovedMeeples>();
		switch (turn) {
		case ("red"):
			land = this.checkBlue(gameState, meeple);
			if (land.isMoved())
				moved.addAll(land.getMovedMeeples());
			gameState = land.getGameState();
			land = this.checkGreen(gameState, meeple);
			if (land.isMoved())
				moved.addAll(land.getMovedMeeples());
			gameState = land.getGameState();
			land = this.checkYellow(gameState, meeple);
			if (land.isMoved())
				moved.addAll(land.getMovedMeeples());
			gameState = land.getGameState();
			break;
		case ("blue"):
			System.out.println(gameState);
			land = this.checkRed(gameState, meeple);
			if (land.isMoved())
				moved.addAll(land.getMovedMeeples());
			gameState = land.getGameState();
			System.out.println(gameState);
			land = this.checkGreen(gameState, meeple);
			if (land.isMoved())
				moved.addAll(land.getMovedMeeples());
			gameState = land.getGameState();
			land = this.checkYellow(gameState, meeple);
			if (land.isMoved())
				moved.addAll(land.getMovedMeeples());
			gameState = land.getGameState();
			break;
		case ("green"):
			land = this.checkBlue(gameState, meeple);
			if (land.isMoved())
				moved.addAll(land.getMovedMeeples());
			gameState = land.getGameState();
			land = this.checkRed(gameState, meeple);
			if (land.isMoved())
				moved.addAll(land.getMovedMeeples());
			gameState = land.getGameState();
			land = this.checkYellow(gameState, meeple);
			if (land.isMoved())
				moved.addAll(land.getMovedMeeples());
			gameState = land.getGameState();
			break;
		case ("yellow"):
			land = this.checkBlue(gameState, meeple);
			if (land.isMoved())
				moved.addAll(land.getMovedMeeples());
			gameState = land.getGameState();
			land = this.checkGreen(gameState, meeple);
			if (land.isMoved())
				moved.addAll(land.getMovedMeeples());
			gameState = land.getGameState();
			land = this.checkRed(gameState, meeple);
			if (land.isMoved())
				moved.addAll(land.getMovedMeeples());
			gameState = land.getGameState();
			break;
		default:
			break;
		}
		land.setMovedMeeples(moved);
		land.setGameState(gameState);
		return land;
	}

	public PlayerAndLand checkRed(GameState gameState, Meeple meeple) {
		PlayerAndLand land = new PlayerAndLand();
		Meeple kickedMeeple = new Meeple();
		Player player = gameState.getRedPlayer();
		int victimas = 0;
		int kicked = 0;
		if (meeple.getPosition() == gameState.getRedPlayer().getMeeple1().getPosition()) {
			kickedMeeple = gameState.getRedPlayer().getMeeple1();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple1(new Meeple());
			victimas++;
			kicked = 1;
		}
		if (meeple.getPosition() == gameState.getRedPlayer().getMeeple2().getPosition()) {
			kickedMeeple = gameState.getRedPlayer().getMeeple2();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple2(new Meeple());
			victimas++;
			kicked = 2;
		}
		if (meeple.getPosition() == gameState.getRedPlayer().getMeeple3().getPosition()) {
			kickedMeeple = gameState.getRedPlayer().getMeeple3();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple3(new Meeple());
			victimas++;
			kicked = 3;
		}
		if (meeple.getPosition() == gameState.getRedPlayer().getMeeple4().getPosition()) {
			kickedMeeple = gameState.getRedPlayer().getMeeple4();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple4(new Meeple());
			victimas++;
			kicked = 4;
		}
		if (victimas == 1) {
			gameState.setRedPlayer(player);
			gameState.setExtraTurn(true);
			MovedMeeples move = new MovedMeeples();
			move.setPlayerId(player.getId());
			move.setMeeple(kicked);
			move.setInitialPosition(meeple.getPosition());
			move.setFinalPosition(0);
			List<MovedMeeples> moved = new ArrayList<MovedMeeples>();
			moved.add(move);
			land.setMoved(true);
			land.setMovedMeeples(moved);
			System.out.println("Lastima de vuelta a casa! Obtienes otra tirada!");
		}
		land.setGameState(gameState);
		return land;
	}

	public PlayerAndLand checkBlue(GameState gameState, Meeple meeple) {
		PlayerAndLand land = new PlayerAndLand();
		Meeple kickedMeeple = new Meeple();
		Player player = gameState.getBluePlayer();
		int victimas = 0;
		int kicked = 0;
		if (meeple.getPosition() == gameState.getBluePlayer().getMeeple1().getPosition()) {
			kickedMeeple = gameState.getBluePlayer().getMeeple1();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple1(kickedMeeple);
			victimas++;
			kicked = 1;
		}
		if (meeple.getPosition() == gameState.getBluePlayer().getMeeple2().getPosition()) {
			kickedMeeple = gameState.getBluePlayer().getMeeple2();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple2(new Meeple());
			victimas++;
			kicked = 2;
		}
		if (meeple.getPosition() == gameState.getBluePlayer().getMeeple3().getPosition()) {
			kickedMeeple = gameState.getBluePlayer().getMeeple3();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple3(new Meeple());
			victimas++;
			kicked = 3;
		}
		if (meeple.getPosition() == gameState.getBluePlayer().getMeeple4().getPosition()) {
			kickedMeeple = gameState.getBluePlayer().getMeeple4();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple4(new Meeple());
			victimas++;
			kicked = 4;
		}
		if (victimas == 1) {
			gameState.setBluePlayer(player);
			gameState.setExtraTurn(true);
			MovedMeeples move = new MovedMeeples();
			move.setPlayerId(player.getId());
			move.setMeeple(kicked);
			move.setInitialPosition(meeple.getPosition());
			move.setFinalPosition(0);
			List<MovedMeeples> moved = new ArrayList<MovedMeeples>();
			moved.add(move);
			land.setMoved(true);
			land.setMovedMeeples(moved);
			System.out.println("Lastima de vuelta a casa! Obtienes otra tirada!");
		}
		land.setGameState(gameState);
		return land;
	}

	public PlayerAndLand checkGreen(GameState gameState, Meeple meeple) {
		PlayerAndLand land = new PlayerAndLand();
		Meeple kickedMeeple = new Meeple();
		Player player = gameState.getGreenPlayer();
		int victimas = 0;
		int kicked = 0;
		if (meeple.getPosition() == gameState.getGreenPlayer().getMeeple1().getPosition()) {
			kickedMeeple = gameState.getGreenPlayer().getMeeple1();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple1(kickedMeeple);
			victimas++;
			kicked = 1;
		}
		if (meeple.getPosition() == gameState.getGreenPlayer().getMeeple2().getPosition()) {
			kickedMeeple = gameState.getGreenPlayer().getMeeple2();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple2(new Meeple());
			victimas++;
			kicked = 2;
		}
		if (meeple.getPosition() == gameState.getGreenPlayer().getMeeple3().getPosition()) {
			kickedMeeple = gameState.getGreenPlayer().getMeeple3();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple3(new Meeple());
			victimas++;
			kicked = 3;
		}
		if (meeple.getPosition() == gameState.getGreenPlayer().getMeeple4().getPosition()) {
			kickedMeeple = gameState.getGreenPlayer().getMeeple4();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple4(new Meeple());
			victimas++;
			kicked = 4;
		}
		if (victimas == 1) {
			gameState.setGreenPlayer(player);
			gameState.setExtraTurn(true);
			MovedMeeples move = new MovedMeeples();
			move.setPlayerId(player.getId());
			move.setMeeple(kicked);
			move.setInitialPosition(meeple.getPosition());
			move.setFinalPosition(0);
			List<MovedMeeples> moved = new ArrayList<MovedMeeples>();
			moved.add(move);
			land.setMoved(true);
			land.setMovedMeeples(moved);
			System.out.println("Lastima de vuelta a casa! Obtienes otra tirada!");
		}
		land.setGameState(gameState);
		return land;
	}

	public PlayerAndLand checkYellow(GameState gameState, Meeple meeple) {
		PlayerAndLand land = new PlayerAndLand();
		Meeple kickedMeeple = new Meeple();
		Player player = gameState.getYellowPlayer();
		int victimas = 0;
		int kicked = 0;
		if (meeple.getPosition() == gameState.getYellowPlayer().getMeeple1().getPosition()) {
			kickedMeeple = gameState.getYellowPlayer().getMeeple1();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple1(kickedMeeple);
			victimas++;
			kicked = 1;
		}
		if (meeple.getPosition() == gameState.getYellowPlayer().getMeeple2().getPosition()) {
			kickedMeeple = gameState.getYellowPlayer().getMeeple2();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple2(new Meeple());
			victimas++;
			kicked = 2;
		}
		if (meeple.getPosition() == gameState.getYellowPlayer().getMeeple3().getPosition()) {
			kickedMeeple = gameState.getYellowPlayer().getMeeple3();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple3(new Meeple());
			victimas++;
			kicked = 3;
		}
		if (meeple.getPosition() == gameState.getYellowPlayer().getMeeple4().getPosition()) {
			kickedMeeple = gameState.getYellowPlayer().getMeeple4();
			kickedMeeple.setPosition(0);
			kickedMeeple.setRelativePosition(0);
			player.setMeeple4(new Meeple());
			victimas++;
			kicked = 4;
		}
		if (victimas == 1) {
			gameState.setYellowPlayer(player);
			gameState.setExtraTurn(true);
			MovedMeeples move = new MovedMeeples();
			move.setPlayerId(player.getId());
			move.setMeeple(kicked);
			move.setInitialPosition(meeple.getPosition());
			move.setFinalPosition(0);
			List<MovedMeeples> moved = new ArrayList<MovedMeeples>();
			moved.add(move);
			land.setMoved(true);
			land.setMovedMeeples(moved);
			System.out.println("Lastima de vuelta a casa! Obtienes otra tirada!");
		}
		land.setGameState(gameState);
		return land;
	}

	public boolean isCellProtected(int cell) {
		if (cell == 1 || cell == 9 || cell == 14 || cell == 22 || cell == 27 || cell == 35 || cell == 40
				|| cell == 48) {
			System.out.println("Salvado por la campana!");
			return true;
		} else {
			return false;
		}
	}

}
