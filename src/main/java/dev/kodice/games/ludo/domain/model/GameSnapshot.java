package dev.kodice.games.ludo.domain.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class GameSnapshot {
	
	private Long gId;

	private boolean sRoll;

	private int sRolled;

	private boolean sMove;

	private Long pId;
	
	private String pKey;
	
	private boolean pTurn;
	
	@Id
	private Long mId;
	
	private int mPos;
	
	private int mRel;
	
}
