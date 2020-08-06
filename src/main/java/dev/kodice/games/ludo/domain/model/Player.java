package dev.kodice.games.ludo.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Proxy;

import lombok.Data;

@Data
@Entity
@Proxy(lazy=false)
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "[key]")
	private String key;

	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name = "PLAYER_MEEPLE", joinColumns = {
			@JoinColumn(name = "player_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "meeple_id", referencedColumnName = "id") })
	private List<Meeple> meeples;

	private Boolean turn;

	public Player() {
		List<Meeple> meeples = new ArrayList<Meeple>();
		for (int i = 0; i <= 3; i++) {
			meeples.add(new Meeple());
		}
		this.meeples = meeples;
		this.turn = false;
	}
	
	public void resetMeeple(int meeple) {
		Meeple m = this.meeples.get(meeple-1);
		m.setPosition(0);
		m.setRelativePosition(0);
		this.meeples.set(meeple-1, m);
	}

	public Player(Long id, String key, Boolean turn) {
		this.id = id;
		this.key = key;
		this.turn = turn;
	}

}
