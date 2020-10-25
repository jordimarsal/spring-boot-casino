package net.jordimp.casino.services.dto;

import java.io.Serializable;

public class Bet implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String W_NO_TIME = "Player not logged or timed out";

	public static final String W_NO_GAME = "BAD Game ID";

	public static final String W_LIMIT_BET = "Bet out of bounds";

	public static final String WIN = "WIN! You have earned %s credits!!!";

	public static final String E_NULL = "NULL values in Bet";

	private String betUUID;

	private Double betAmount;

	private String playerUUID;

	private String gameUUID;

	private Double balancePlayer;

	private Double prizeAmount;

	private String comment;

	private String warning;

	private Boolean bad = false;

	public Bet(Double amount, String playerUUID, String gameUUID, Double balancePlayer) {
		super();
		this.betAmount = amount;
		this.playerUUID = playerUUID;
		this.gameUUID = gameUUID;
		this.setBalancePlayer(balancePlayer);
	}

	public String getBetUUID() {
		return betUUID;
	}

	public void setBetUUID(String betUUID) {
		this.betUUID = betUUID;
	}

	public Double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Double amount) {
		this.betAmount = amount;
	}

	public String getPlayerUUID() {
		return playerUUID;
	}

	public void setPlayerUUID(String playerUUID) {
		this.playerUUID = playerUUID;
	}

	public String getGameUUID() {
		return gameUUID;
	}

	public void setGameUUID(String gameUUID) {
		this.gameUUID = gameUUID;
	}

	public Double getBalancePlayer() {
		return balancePlayer;
	}

	public void setBalancePlayer(Double balancePlayer) {
		this.balancePlayer = balancePlayer;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getPrizeAmount() {
		return prizeAmount;
	}

	public void setPrizeAmount(Double prizeAmount) {
		this.prizeAmount = prizeAmount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public Boolean isBad() {
		return bad;
	}

	public void setBad(Boolean bad) {
		this.bad = bad;
	}

	@Override
	public String toString() {
		return "Bet [betUUID=" + betUUID + ", gameUUID=" + gameUUID + ", prizeAmount=" + prizeAmount + ", comment="
				+ comment + ", balancePlayer=" + balancePlayer + "]";
	}

	public String constructor() {
		return "Bet [betAmount=" + betAmount + ", playerUUID=" + playerUUID + ", gameUUID=" + gameUUID
				+ ", balancePlayer=" + balancePlayer + "]";
	}

}
