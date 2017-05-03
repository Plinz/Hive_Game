package main.java.model;

public class Player {
	
	private String name;
	private String color;
	
	
	public Player(String name, String color) {
		this.name = name;
		this.color = color;
	}
	
	public Player(){
		this.name = "Anonymous";
		this.color = "White";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

}
