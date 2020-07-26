package dev.kodice.games.ludo.domain.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(name = "[key]")
	private String key;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "PLAYER_MEEPLE1", joinColumns = {
			@JoinColumn(name = "player_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "meeple1_id", referencedColumnName = "id") })
	private Meeple Meeple1;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "PLAYER_MEEPLE2", joinColumns = {
			@JoinColumn(name = "player_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "meeple2_id", referencedColumnName = "id") })
	private Meeple Meeple2;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "PLAYER_MEEPLE3", joinColumns = {
			@JoinColumn(name = "player_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "meeple3_id", referencedColumnName = "id") })
	private Meeple Meeple3;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "PLAYER_MEEPLE4", joinColumns = {
			@JoinColumn(name = "player_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "meeple4_id", referencedColumnName = "id") })
	private Meeple Meeple4;

	private Boolean turn;

	public Player() {
		this.Meeple1 = new Meeple();
		this.Meeple2 = new Meeple();
		this.Meeple3 = new Meeple();
		this.Meeple4 = new Meeple();
		this.turn = false;
	}

}
