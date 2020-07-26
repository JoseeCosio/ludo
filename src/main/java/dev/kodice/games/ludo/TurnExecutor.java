package dev.kodice.games.ludo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import dev.kodice.games.ludo.domain.dto.MovedMeeples;
import dev.kodice.games.ludo.domain.model.GameState;
import dev.kodice.games.ludo.domain.model.Meeple;
import dev.kodice.games.ludo.domain.model.Player;

@Service
public class TurnExecutor {

	public int rollDice() {
		Random rand = new Random();
		return rand.nextInt(6) + 1;
	}

	public int getMeepleToMove(int options) {
		Random rand = new Random();
		return rand.nextInt(options) + 1;
	}

	public boolean canMeepleMove(Meeple meeple, int dice) {
		if (meeple.getPosition() == 0) {
			if (dice != 6)
				return false;
			else
				return true;
		} else {
			if (57 >= (meeple.getRelativePosition() + dice))
				return true;
			else
				return false;
		}
	}

	public MovingMeeples getLegalMoves(Player player, int dice) {
		MovingMeeples meeples = new MovingMeeples();
		boolean moving[] = new boolean[4];
		moving[0] = this.canMeepleMove(player.getMeeple1(), dice);
		moving[1] = this.canMeepleMove(player.getMeeple2(), dice);
		moving[2] = this.canMeepleMove(player.getMeeple3(), dice);
		moving[3] = this.canMeepleMove(player.getMeeple4(), dice);
		meeples.setMeeples(moving);
		int quantity = 0;
		for (int i = 0; i <= 3; i++) {
			if (moving[i])
				quantity++;
		}
		meeples.setMoving(quantity);
		return meeples;
	}

	public Player getPlayerInTurn(GameState gameState) {
		if (gameState.getRedPlayer().getTurn()) {
			System.out.println("Juega el rojo!");
			return gameState.getRedPlayer();
		}
		if (gameState.getBluePlayer().getTurn()) {
			System.out.println("Juega el azul!");
			return gameState.getBluePlayer();
		}
		if (gameState.getGreenPlayer().getTurn()) {
			System.out.println("Juega el verde!");
			return gameState.getGreenPlayer();
		}
		if (gameState.getYellowPlayer().getTurn()) {
			System.out.println("Juega el amarillo!");
			return gameState.getYellowPlayer();
		}
		return null;
	}

	public boolean isPlayerInTurn(Player player) {
		return player.getTurn();
	}

	public Meeple getLandingCell(Meeple meeple, int dice, String turn) {
		switch (turn) {
		case ("red"):
			if (meeple.getPosition() == 0) {
				meeple.setPosition(1);
				meeple.setRelativePosition(1);
			} else {
				meeple.setRelativePosition(meeple.getRelativePosition() + dice);
				if (meeple.getRelativePosition() > 51) {
					meeple.setPosition(meeple.getRelativePosition() + 1);
				} else
					meeple.setPosition(meeple.getPosition() + dice);
			}
			break;
		case ("blue"):
			if (meeple.getPosition() == 0) {
				meeple.setPosition(14);
				meeple.setRelativePosition(1);
			} else {
				meeple.setRelativePosition(meeple.getRelativePosition() + dice);
				if (meeple.getRelativePosition() > 51) {
					meeple.setPosition(meeple.getRelativePosition() + 7);
				} else {
					if (meeple.getPosition() == 52) {
						meeple.setPosition(52);
						System.out.println("Aquí no te salvas!");
					} else {
						meeple.setPosition((meeple.getPosition() + dice) % 52);
					}
				}
			}
			break;
		case ("green"):
			if (meeple.getPosition() == 0) {
				meeple.setPosition(27);
				meeple.setRelativePosition(1);
			} else {
				meeple.setRelativePosition(meeple.getRelativePosition() + dice);
				if (meeple.getRelativePosition() > 51) {
					meeple.setPosition(meeple.getRelativePosition() + 13);
				} else {
					if (meeple.getPosition() == 52) {
						meeple.setPosition(52);
						System.out.println("Aquí no te salvas!");
					} else {
						meeple.setPosition((meeple.getPosition() + dice) % 52);
					}
				}
			}
			break;
		case ("yellow"):
			if (meeple.getPosition() == 0) {
				meeple.setPosition(40);
				meeple.setRelativePosition(1);
			} else {
				meeple.setRelativePosition(meeple.getRelativePosition() + dice);
				if (meeple.getRelativePosition() > 51) {
					meeple.setPosition(meeple.getRelativePosition() + 19);
				} else {
					if (meeple.getPosition() == 52) {
						meeple.setPosition(52);
						System.out.println("Aquí no te salvas!");
					} else {
						meeple.setPosition((meeple.getPosition() + dice) % 52);
					}
				}
			}
			break;
		default:
			break;
		}

		return meeple;
	}

	public PlayerAndLand moveMeepleInPlayer(Player player, int dice, int moving, String turn) {
		int check = 0;
		int meepleNumber = 1;
		Meeple meeple;
		Meeple land = new Meeple();
		PlayerAndLand playerAndLand = new PlayerAndLand();
		MovedMeeples move = new MovedMeeples();
		List<MovedMeeples> moved = new ArrayList<MovedMeeples>();
		if (this.canMeepleMove(player.getMeeple1(), dice)) {
			check++;
			if (check == moving) {
				meeple = player.getMeeple1();
				move.setInitialPosition(meeple.getPosition());
				playerAndLand.setInitialMeeple(meeple);
				land = this.getLandingCell(meeple, dice, turn);
				player.setMeeple1(land);
				move.setPlayerId(player.getId());
				move.setMeeple(meepleNumber);
				move.setFinalPosition(land.getPosition());
				moved.add(move);
				playerAndLand.setMovedMeeples(moved);
				playerAndLand.setMoved(true);
				check = 5;
			}
		}
		meepleNumber = 2;
		if (this.canMeepleMove(player.getMeeple2(), dice)) {
			check++;
			if (check == moving) {
				meeple = player.getMeeple2();
				move.setInitialPosition(meeple.getPosition());
				playerAndLand.setInitialMeeple(meeple);
				land = this.getLandingCell(meeple, dice, turn);
				player.setMeeple2(land);
				move.setPlayerId(player.getId());
				move.setMeeple(meepleNumber);
				move.setFinalPosition(land.getPosition());
				moved.add(move);
				playerAndLand.setMovedMeeples(moved);
				playerAndLand.setMoved(true);
				check = 5;
			}
		}
		meepleNumber = 3;
		if (this.canMeepleMove(player.getMeeple3(), dice)) {
			check++;
			if (check == moving) {
				meeple = player.getMeeple3();
				move.setInitialPosition(meeple.getPosition());
				playerAndLand.setInitialMeeple(meeple);
				land = this.getLandingCell(meeple, dice, turn);
				player.setMeeple3(land);
				move.setPlayerId(player.getId());
				move.setMeeple(meepleNumber);
				move.setFinalPosition(land.getPosition());
				moved.add(move);
				playerAndLand.setMovedMeeples(moved);
				playerAndLand.setMoved(true);
				check = 5;
			}
		}
		meepleNumber = 4;
		if (this.canMeepleMove(player.getMeeple4(), dice)) {
			check++;
			if (check == moving) {
				meeple = player.getMeeple4();
				move.setInitialPosition(meeple.getPosition());
				playerAndLand.setInitialMeeple(meeple);
				land = this.getLandingCell(meeple, dice, turn);
				player.setMeeple4(land);
				move.setPlayerId(player.getId());
				move.setMeeple(meepleNumber);
				move.setFinalPosition(land.getPosition());
				moved.add(move);
				playerAndLand.setMovedMeeples(moved);
				playerAndLand.setMoved(true);
				check = 5;
			}
		}
		playerAndLand.setPlayer(player);
		playerAndLand.setFinalMeeple(land);
		return playerAndLand;
	}

	public PlayerAndLand moveMeeple(GameState gameState, int dice, int moving) {
		PlayerAndLand playLand = new PlayerAndLand();
		PlayerAndLand kickLand = new PlayerAndLand();
		BackToBase kick = new BackToBase();
		List<MovedMeeples> moved = new ArrayList<MovedMeeples>();
		if (gameState.getRedPlayer().getTurn()) {
			playLand = this.moveMeepleInPlayer(gameState.getRedPlayer(), dice, moving, "red");
			moved.addAll(playLand.getMovedMeeples());
			gameState.setRedPlayer(playLand.getPlayer());
			if (!kick.isCellProtected(playLand.getFinalMeeple().getPosition())) {
				kickLand = kick.kickEnemies(gameState, playLand.getFinalMeeple(), "red");
				if (kickLand.isMoved()) {
					moved.addAll(kickLand.getMovedMeeples());
					gameState = kickLand.getGameState();
				}
			}
			if (playLand.getFinalMeeple().getRelativePosition() == 57) {
				gameState.setExtraTurn(true);
				System.out.println("Meeple llegó a la meta. Obtienes una tirada extra!");
			}
		}
		if (gameState.getBluePlayer().getTurn()) {
			playLand = this.moveMeepleInPlayer(gameState.getBluePlayer(), dice, moving, "blue");
			moved.addAll(playLand.getMovedMeeples());
			gameState.setBluePlayer(playLand.getPlayer());
			System.out.println(gameState);
			if (!kick.isCellProtected(playLand.getFinalMeeple().getPosition()))
				kickLand = kick.kickEnemies(gameState, playLand.getFinalMeeple(), "blue");
			if (kickLand.isMoved()) {
				moved.addAll(kickLand.getMovedMeeples());
				gameState = kickLand.getGameState();
			}
			if (playLand.getFinalMeeple().getRelativePosition() == 57) {
				gameState.setExtraTurn(true);
				System.out.println("Meeple llegó a la meta. Obtienes una tirada extra!");
			}
		}
		System.out.println(gameState);
		if (gameState.getGreenPlayer().getTurn()) {
			playLand = this.moveMeepleInPlayer(gameState.getGreenPlayer(), dice, moving, "green");
			moved.addAll(playLand.getMovedMeeples());
			gameState.setGreenPlayer(playLand.getPlayer());
			if (!kick.isCellProtected(playLand.getFinalMeeple().getPosition()))
				kickLand = kick.kickEnemies(gameState, playLand.getFinalMeeple(), "green");
			if (kickLand.isMoved()) {
				moved.addAll(kickLand.getMovedMeeples());
				gameState = kickLand.getGameState();
			}
			if (playLand.getFinalMeeple().getRelativePosition() == 57) {
				gameState.setExtraTurn(true);
				System.out.println("Meeple llegó a la meta. Obtienes una tirada extra!");
			}
		}
		if (gameState.getYellowPlayer().getTurn()) {
			playLand = this.moveMeepleInPlayer(gameState.getYellowPlayer(), dice, moving, "yellow");
			moved.addAll(playLand.getMovedMeeples());
			gameState.setYellowPlayer(playLand.getPlayer());
			if (!kick.isCellProtected(playLand.getFinalMeeple().getPosition()))
				kickLand = kick.kickEnemies(gameState, playLand.getFinalMeeple(), "yellow");
			if (kickLand.isMoved()) {
				moved.addAll(kickLand.getMovedMeeples());
				gameState = kickLand.getGameState();
			}
			if (playLand.getFinalMeeple().getRelativePosition() == 57) {
				gameState.setExtraTurn(true);
				System.out.println("Meeple llegó a la meta. Obtienes una tirada extra!");
			}
		}
		playLand.setMovedMeeples(moved);
		playLand.setGameState(gameState);
		return playLand;
	}

	public GameState passTurn(GameState gameState) {
		Player player;
		if (gameState.getRedPlayer().getTurn()) {
			player = gameState.getRedPlayer();
			player.setTurn(false);
			gameState.setRedPlayer(player);
			player = gameState.getBluePlayer();
			player.setTurn(true);
			gameState.setBluePlayer(player);
			return gameState;
		}
		if (gameState.getBluePlayer().getTurn()) {
			player = gameState.getBluePlayer();
			player.setTurn(false);
			gameState.setBluePlayer(player);
			player = gameState.getGreenPlayer();
			player.setTurn(true);
			gameState.setGreenPlayer(player);
			return gameState;
		}
		if (gameState.getGreenPlayer().getTurn()) {
			player = gameState.getGreenPlayer();
			player.setTurn(false);
			gameState.setGreenPlayer(player);
			player = gameState.getYellowPlayer();
			player.setTurn(true);
			gameState.setYellowPlayer(player);
			return gameState;
		}
		if (gameState.getYellowPlayer().getTurn()) {
			player = gameState.getYellowPlayer();
			player.setTurn(false);
			gameState.setYellowPlayer(player);
			player = gameState.getRedPlayer();
			player.setTurn(true);
			gameState.setRedPlayer(player);
			return gameState;
		}
		return gameState;
	}

}
