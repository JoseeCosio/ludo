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

	private Long pId;
	
	private String pKey;
	
	private boolean pTurn;
	
	@Id
	private Long mId;
	
	private int mPos;
	
	private int mRel;
	
	public FrontSnapshot mapToFrontSnapshot() {
		FrontSnapshot snap = new FrontSnapshot();
		snap.setSRoll(this.sRoll);
		snap.setSRolled(this.sRolled);
		snap.setPTurn(this.pTurn);
		snap.setMPos(this.mPos);
		return snap;
	}
	
}
