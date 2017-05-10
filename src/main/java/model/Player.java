package main.java.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.java.model.piece.Ant;
import main.java.model.piece.Beetle;
import main.java.model.piece.Grasshopper;
import main.java.model.piece.Queen;
import main.java.model.piece.Spider;
import main.java.utils.Consts;

public class Player implements Serializable{
	
	private static final long serialVersionUID = 4185691264228822669L;
	private String name;
	private int team;
	private List<Piece> inventory;
	
	
	public Player(String name, int team) {
		this.name = name;
		this.team = team;
		this.inventory = new ArrayList<Piece>();
		this.init();
	}
	
	public Player(int team){
		this.name = "Anonymous";
		this.team = team;
		this.inventory = new ArrayList<Piece>();
		this.init();
	}
	
	public Player(Player player1) {
		this.name = new String(player1.getName());
		this.team = player1.getTeam();
		this.inventory = new ArrayList<Piece>();
		for (Piece p : player1.getInventory()){
			this.inventory.add(p.clone());
		}
	}

	public List<Piece> getInventory() {
		return inventory;
	}
	public void setInventory(List<Piece> inventory) {
		this.inventory = inventory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTeam() {
		return team;
	}
	public void setTeam(int team) {
		this.team = team;
	}

	private void init(){
		this.inventory.add(new Queen(Consts.QUEEN, team));
		this.inventory.add(new Spider(Consts.SPIDER1, team));
		this.inventory.add(new Spider(Consts.SPIDER2, team));
		this.inventory.add(new Grasshopper(Consts.GRASSHOPPER1, team));
		this.inventory.add(new Grasshopper(Consts.GRASSHOPPER2, team));
		this.inventory.add(new Grasshopper(Consts.GRASSHOPPER3, team));
		this.inventory.add(new Beetle(Consts.BEETLE1, team));
		this.inventory.add(new Beetle(Consts.BEETLE2, team));
		this.inventory.add(new Ant(Consts.ANT1, team));
		this.inventory.add(new Ant(Consts.ANT2, team));
		this.inventory.add(new Ant(Consts.ANT3, team));
	}
	
	public Piece removePiece(int piece) {
		return this.inventory.remove(piece);		
	}

}
