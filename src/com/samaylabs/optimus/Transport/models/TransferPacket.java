package com.samaylabs.optimus.Transport.models;

public class TransferPacket {

	private long currentAnchor;
	private int currentAction;
	private int currentType;
	private boolean cMove;
	
	private long nextAnchor;
	private int nextAction;
	private int nextType;
	private boolean nMove;
	
	public TransferPacket(long currentAnchor,int currentAction, int currentType, boolean cMove, long nextAnchor, int nextAction, int nextType,boolean nMove) {
		this.currentAnchor = currentAnchor;
		this.currentAction = currentAction;
		this.currentType = currentType;
		this.cMove = cMove;
		this.nextAnchor = nextAnchor;
		this.nextAction = nextAction;
		this.nextType = nextType;
		this.nMove = nMove;
	}
	
	public long getCurrentAnchor() {
		return currentAnchor;
	}
	public void setCurrentAnchor(long currentAnchor) {
		this.currentAnchor = currentAnchor;
	}
	public int getCurrentAction() {
		return currentAction;
	}
	public void setCurrentAction(int currentAction) {
		this.currentAction = currentAction;
	}
	public int getCurrentType() {
		return currentType;
	}
	public void setCurrentType(int currentType) {
		this.currentType = currentType;
	}
	public boolean getcMove() {
		return cMove;
	}
	
	public void setcMove(boolean cMove) {
		this.cMove = cMove;
	}
	public long getNextAnchor() {
		return nextAnchor;
	}
	public void setNextAnchor(long nextAnchor) {
		this.nextAnchor = nextAnchor;
	}
	public int getNextAction() {
		return nextAction;
	}
	public void setNextAction(int nextAction) {
		this.nextAction = nextAction;
	}
	public int getNextType() {
		return nextType;
	}
	public void setNextType(int nextType) {
		this.nextType = nextType;
	}
	public boolean getnMove() {
		return nMove;
	}
	public void setnMove(boolean nMove) {
		this.nMove = nMove;
	}

	@Override
	public String toString() {
		return "[CAnchor=" + currentAnchor + ", CAction=" + currentAction + ", CType="
				+ currentType + ", CMove=" + cMove + ", NAnchor=" + nextAnchor + ", NAction=" + nextAction
				+ ", NType=" + nextType + ", NMove=" + nMove + "]";
	}
	
	
}
