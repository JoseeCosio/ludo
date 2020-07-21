package dev.kodice.games.ludo;

public class BackToBase {

	public GameState kickEnemies(GameState gameState, Meeple meeple, String turn) {
		switch (turn) {
		case ("red"):
			gameState = this.checkBlue(gameState, meeple);
			gameState = this.checkGreen(gameState, meeple);
			gameState = this.checkYellow(gameState, meeple);
			break;
		case ("blue"):
			gameState = this.checkRed(gameState, meeple);
			gameState = this.checkGreen(gameState, meeple);
			gameState = this.checkYellow(gameState, meeple);
			break;
		case ("green"):
			gameState = this.checkBlue(gameState, meeple);
			gameState = this.checkRed(gameState, meeple);
			gameState = this.checkYellow(gameState, meeple);
			break;
		case ("yellow"):
			gameState = this.checkBlue(gameState, meeple);
			gameState = this.checkGreen(gameState, meeple);
			gameState = this.checkRed(gameState, meeple);
			break;
		default:
			break;
		}
		return gameState;
	}

	public GameState checkRed(GameState gameState, Meeple meeple) {
		Player player = gameState.getRedPlayer();
		int victimas = 0;
		if (meeple.getPosition() == gameState.getRedPlayer().getMeeple1().getPosition()) {
			player.setMeeple1(new Meeple());
			victimas++;
		}
		if (meeple.getPosition() == gameState.getRedPlayer().getMeeple2().getPosition()) {
			player.setMeeple2(new Meeple());
			victimas++;
		}
		if (meeple.getPosition() == gameState.getRedPlayer().getMeeple3().getPosition()) {
			player.setMeeple3(new Meeple());
			victimas++;
		}
		if (meeple.getPosition() == gameState.getRedPlayer().getMeeple4().getPosition()) {
			player.setMeeple4(new Meeple());
			victimas++;
		}
		if (victimas == 1) {
			gameState.setRedPlayer(player);
			System.out.println("Lastima de vuelta a casa!");
		}
		return gameState;
	}

	public GameState checkBlue(GameState gameState, Meeple meeple) {
		Player player = gameState.getBluePlayer();
		int victimas = 0;
		if (meeple.getPosition() == gameState.getBluePlayer().getMeeple1().getPosition()) {
			player.setMeeple1(new Meeple());
			victimas++;
		}
		if (meeple.getPosition() == gameState.getBluePlayer().getMeeple2().getPosition()) {
			player.setMeeple2(new Meeple());
			victimas++;
		}
		if (meeple.getPosition() == gameState.getBluePlayer().getMeeple3().getPosition()) {
			player.setMeeple3(new Meeple());
			victimas++;
		}
		if (meeple.getPosition() == gameState.getBluePlayer().getMeeple4().getPosition()) {
			player.setMeeple4(new Meeple());
			victimas++;
		}
		if (victimas == 1) {
			gameState.setBluePlayer(player);
			System.out.println("Lastima de vuelta a casa!");
		}
		return gameState;
	}

	public GameState checkGreen(GameState gameState, Meeple meeple) {
		Player player = gameState.getGreenPlayer();
		int victimas = 0;
		if (meeple.getPosition() == gameState.getGreenPlayer().getMeeple1().getPosition()) {
			player.setMeeple1(new Meeple());
			victimas++;
		}
		if (meeple.getPosition() == gameState.getGreenPlayer().getMeeple2().getPosition()) {
			player.setMeeple2(new Meeple());
			victimas++;
		}
		if (meeple.getPosition() == gameState.getGreenPlayer().getMeeple3().getPosition()) {
			player.setMeeple3(new Meeple());
			victimas++;
		}
		if (meeple.getPosition() == gameState.getGreenPlayer().getMeeple4().getPosition()) {
			player.setMeeple4(new Meeple());
			victimas++;
		}
		if (victimas == 1) {
			gameState.setGreenPlayer(player);
			System.out.println("Lastima de vuelta a casa!");
		}
		return gameState;
	}

	public GameState checkYellow(GameState gameState, Meeple meeple) {
		Player player = gameState.getYellowPlayer();
		int victimas = 0;
		if (meeple.getPosition() == gameState.getYellowPlayer().getMeeple1().getPosition()) {
			player.setMeeple1(new Meeple());
			victimas++;
		}
		if (meeple.getPosition() == gameState.getYellowPlayer().getMeeple2().getPosition()) {
			player.setMeeple2(new Meeple());
			victimas++;
		}
		if (meeple.getPosition() == gameState.getYellowPlayer().getMeeple3().getPosition()) {
			player.setMeeple3(new Meeple());
			victimas++;
		}
		if (meeple.getPosition() == gameState.getYellowPlayer().getMeeple4().getPosition()) {
			player.setMeeple4(new Meeple());
			victimas++;
		}
		if (victimas == 1) {
			gameState.setYellowPlayer(player);
			System.out.println("Lastima de vuelta a casa!");
		}
		return gameState;
	}
	
	public boolean isCellProtected(int cell) {
		if(cell==1 || cell==9 || cell==14 || cell==22 || cell==27 || cell==35 || cell==40 || cell==48) {
			System.out.println("Salvado por la campana!");
			return true;
		}
		else {
			return false;
		}
	}

}
