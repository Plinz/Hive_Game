package main.java.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import main.java.model.piece.Ant;
import main.java.model.piece.Beetle;
import main.java.model.piece.Grasshopper;
import main.java.model.piece.Queen;
import main.java.model.piece.Spider;
import main.java.utils.Consts;

@XmlRootElement(name="player")
@XmlAccessorType(XmlAccessType.FIELD)
public class Player{
	
    @XmlElement(name="name")
	private String name;
    @XmlAttribute(name="team")
	private int team;
    @XmlElementWrapper(name="inv")
    @XmlElements({
        @XmlElement(name="ant", type=Ant.class),
        @XmlElement(name="beetle", type=Beetle.class),
        @XmlElement(name="grasshopper", type=Grasshopper.class),
//        @XmlElement(name="ladybug", type=Ladybug.class),
//        @XmlElement(name="mosquito", type=Mosquito.class),
//        @XmlElement(name="pillbug", type=Pillbug.class),
        @XmlElement(name="queen", type=Queen.class),
        @XmlElement(name="spider", type=Spider.class)
    })
	private List<Piece> inventory;
	
    public Player(){
    	this.team = -1;
    }
    
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
		inventory.add(new Queen(Consts.QUEEN, team));
		inventory.add(new Spider(Consts.SPIDER1, team));
		inventory.add(new Spider(Consts.SPIDER2, team));
		inventory.add(new Grasshopper(Consts.GRASSHOPPER1, team));
		inventory.add(new Grasshopper(Consts.GRASSHOPPER2, team));
		inventory.add(new Grasshopper(Consts.GRASSHOPPER3, team));
		inventory.add(new Beetle(Consts.BEETLE1, team));
		inventory.add(new Beetle(Consts.BEETLE2, team));
		inventory.add(new Ant(Consts.ANT1, team));
		inventory.add(new Ant(Consts.ANT2, team));
		inventory.add(new Ant(Consts.ANT3, team));
	}
	
	public Piece removePiece(int pieceId) {
		int type = Consts.getType(pieceId);
		Piece piece = inventory.stream().filter(p -> Consts.getType(p.getId()) == type).min(Comparator.comparing(Piece::getId)).orElse(null);
		inventory.remove(piece);
		return piece;		
	}

	public void addPiece(Piece piece) {
		int i;
		for(i=0; inventory.get(i).getId() < piece.getId(); i++);
		inventory.add(i, piece);
	}

}
