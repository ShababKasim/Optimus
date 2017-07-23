package com.samaylabs.optimus.Transport.models;


/**
 * This is POJO class which holds data which is transfered to Agv while its moving
 * @author Tulve Shabab Kasim
 *
 */
public class TransferPacket {

	private long currentAnchor;
	private int currentAction;
	private int currentType;
	private boolean cMove;
	
	private long nextAnchor;
	private int nextAction;
	private int nextType;
	private boolean nMove;
	
	public TransferPacket() {
		this(0,0,0,false,0,0,0,false);
	}
	
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
	
	public boolean compare(TransferPacket tp){
		if(this.currentAnchor == tp.currentAnchor && this.currentAction == tp.currentAction && this.currentType == tp.currentType &&
				this.cMove == tp.cMove && this.nextAnchor == tp.nextAnchor && this.nextAction == tp.nextAction && this.nextType == tp.nextType && this.nMove == tp.nMove)
			return true;
		return false;
	}

	public void update(TransferPacket tp){
		this.currentAnchor = tp.getCurrentAnchor();
		this.currentAction = tp.getCurrentAction();
		this.currentType = tp.getCurrentType();
		this.cMove = tp.getcMove();
		this.nextAnchor = tp.getNextAnchor();
		this.nextAction = tp.getNextAction();
		this.nextType = tp.getNextType();
		this.nMove = tp.getnMove();
	}
	
	@Override
	public String toString() {
		return "[CAnchor=" + currentAnchor + ", CAction=" + currentAction + ", CType="
				+ currentType + ", CMove=" + cMove + ", NAnchor=" + nextAnchor + ", NAction=" + nextAction
				+ ", NType=" + nextType + ", NMove=" + nMove + "]";
	}
	
	
}
