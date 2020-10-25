package net.jordimp.casino.entity;

public enum UserProvider {
	BWIN("BWIN-UUID"), POKERSTAR("PokerStar-UUID"), OTHER("Other-UUID");

	private String name;

	UserProvider(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
