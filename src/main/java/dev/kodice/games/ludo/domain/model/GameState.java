package dev.kodice.games.ludo.domain.model;

import java.util.Random;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class GameState {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "GAME_STATE_RED_PLAYER", joinColumns = {
			@JoinColumn(name = "game_state_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "red_player_id", referencedColumnName = "id") })
	private Player redPlayer;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "GAME_STATE_BLUE_PLAYER", joinColumns = {
			@JoinColumn(name = "game_state_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "blue_player_id", referencedColumnName = "id") })
	private Player bluePlayer;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "GAME_STATE_GREEN_PLAYER", joinColumns = {
			@JoinColumn(name = "game_state_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "green_player_id", referencedColumnName = "id") })
	private Player greenPlayer;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "GAME_STATE_YELLOW_PLAYER", joinColumns = {
			@JoinColumn(name = "game_state_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "yellow_player_id", referencedColumnName = "id") })
	private Player yellowPlayer;

	private boolean extraTurn;

	private boolean roll;

	private boolean moving;
	
	private int rolled;

	public GameState() {
		Random ran = new Random();
		int turn = ran.nextInt(4) + 1;
		Player player = new Player();
		player.setTurn(true);
		if (turn == 1)
			this.redPlayer = player;
		else
			this.redPlayer = new Player();
		if (turn == 2)
			this.bluePlayer = player;
		else
			this.bluePlayer = new Player();
		if (turn == 3)
			this.greenPlayer = player;
		else
			this.greenPlayer = new Player();
		if (turn == 4)
			this.yellowPlayer = player;
		else
			this.yellowPlayer = new Player();
		this.extraTurn = false;
		this.roll = true;
		this.rolled = 0;
		this.moving = false;
	}

}
