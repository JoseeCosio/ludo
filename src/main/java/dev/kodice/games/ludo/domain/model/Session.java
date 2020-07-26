package dev.kodice.games.ludo.domain.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import dev.kodice.games.ludo.domain.model.Session;
import dev.kodice.games.ludo.util.JsonLocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
@Entity
@Table(name = "Session")
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "session_id")
	private Long id;

	@Column(name = "[key]")
	private String key;

	@Column(name = "valid_until")
	@JsonSerialize(using = JsonLocalDateTimeSerializer.class)
	private LocalDateTime validUntil;

	@Column(name = "player_id")
	private Long playerId;

}