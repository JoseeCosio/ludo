package dev.kodice.games.ludo.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import dev.kodice.games.ludo.domain.dto.Landing;
import dev.kodice.games.ludo.domain.dto.MovedMeeple;
import lombok.Data;

@Data
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "GAME_PLAYER", joinColumns = {
			@JoinColumn(name = "game_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "player_id", referencedColumnName = "id") })
	private List<Player> players;

	private boolean extraTurn;

	private boolean roll;

	private int rolled;

	public Game() {
		Random ran = new Random();
		int turn = ran.nextInt(4) + 1;
		List<Player> players = new ArrayList<Player>();
		for (int i = 1; i <= 4; i++) {
			Player player = new Player();
			if (i == turn) {
				player.setTurn(true);
			}
			players.add(player);
		}
		this.extraTurn = false;
		this.roll = true;
		this.rolled = 0;
		this.players = players;
	}

	public Game(int playerNumber) {
		Random ran = new Random();
		int turn = ran.nextInt(playerNumber) + 1;
		List<Player> players = new ArrayList<Player>();
		for (int i = 1; i <= playerNumber; i++) {
			Player player = new Player();
			if (i == turn) {
				player.setTurn(true);
			}
			players.add(player);
		}
		this.extraTurn = false;
		this.roll = true;
		this.rolled = 0;
		this.players = players;
	}

	public Meeple getMeeple(int player, int meeple) {
		return this.getPlayers().get(player - 1).getMeeples().get(meeple - 1);
	}

	public void updateMeeple(int player, int meeple, Landing landing) {
		Player p = this.players.get(player - 1);
		Meeple m = p.getMeeples().get(meeple - 1);
		m.setPosition(landing.getPosition());
		m.setRelativePosition(landing.getRelativePosition());
		List<Meeple> meeples = p.getMeeples();
		meeples.set(meeple - 1, m);
		p.setMeeples(meeples);
		this.players.set(player - 1, p);
	}

	public void resetMeeple(int player, int meeple) {
		Player p = this.players.get(player - 1);
		p.resetMeeple(meeple);
		this.players.set(player - 1, p);
	}

	
	/* 
	 * Kicks any enemy quantity of meeples if not in protected cell
	 * 
	 * Needed to validate to protect meeples when grouped
	 * 
	 * */
	public List<MovedMeeple> kickMeeples(int landing, int player) {
		List<MovedMeeple> movedMeeples = new ArrayList<MovedMeeple>();
		int kicked = 0;
		int y1 = 0;
		int y2 = 0;
		for (Player p : this.getPlayers()) {
			y1++;
			if (y1 != player) {
				MovedMeeple movedMeeple = new MovedMeeple();
				kicked = 0;
				y2 = 0;
				for (Meeple m : p.getMeeples()) {
					y2++;
					;
					if (m.getPosition() == landing) {
						kicked++;
						movedMeeple.setPlayerId((long) y1);
						movedMeeple.setMeeple(y2);
						movedMeeple.setInitialPosition(landing);
						movedMeeple.setFinalPosition(0);
					}
				}
				if (kicked == 1) {
					movedMeeples.add(movedMeeple);
				}
			}
		}
		if (movedMeeples.size() > 0) {
			System.out.println(movedMeeples);
			for (MovedMeeple m : movedMeeples) {
				this.resetMeeple(m.getPlayerId().intValue(), m.getMeeple());
			}
		}
		return movedMeeples;
	}

}
