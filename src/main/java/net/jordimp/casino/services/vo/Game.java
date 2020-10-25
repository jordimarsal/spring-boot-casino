package net.jordimp.casino.services.vo;

public interface Game {

	String getName();

	String getUUID();

	String getType();

	Double getPrize();

	Double getProbability();

	Double getMinBet();

	Double getMaxBet();

}
