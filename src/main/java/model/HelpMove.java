package main.java.model;

import main.java.utils.CoordGene;

public class HelpMove {
	private boolean isAdd;
	private int pieceId;
	private CoordGene<Integer> from;
	private CoordGene<Integer> target;
	
	public HelpMove(boolean isAdd, int pieceId, CoordGene<Integer> from, CoordGene<Integer> target) {
		this.isAdd = isAdd;
		this.pieceId = pieceId;
		this.from = from;
		this.target = target;
	}
	public boolean isAdd() {
		return isAdd;
	}
	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}
	public int getPieceId() {
		return pieceId;
	}
	public void setPieceId(int pieceId) {
		this.pieceId = pieceId;
	}
	public CoordGene<Integer> getFrom() {
		return from;
	}
	public void setFrom(CoordGene<Integer> from) {
		this.from = from;
	}
	public CoordGene<Integer> getTarget() {
		return target;
	}
	public void setTarget(CoordGene<Integer> target) {
		this.target = target;
	}
	


}
