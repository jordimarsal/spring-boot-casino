package net.jordimp.casino.services.vo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public abstract class BaseGame implements Game {

	@Autowired
	private Environment env;

	protected static Environment environment;

	protected String bprefix;

	@PostConstruct
	public void init() {
		if (env != null) {
			environment = env;
		}
		if (environment != null && bprefix != null) {
			this.name = environment.getProperty(bprefix + ".name");
			this.UUID = environment.getProperty(bprefix + ".uuid");
			this.type = environment.getProperty(bprefix + ".type");
			this.prize = Double.parseDouble(environment.getProperty(bprefix + ".prize"));
			this.probability = Double.parseDouble(environment.getProperty(bprefix + ".prob"));
			this.minBet = Double.parseDouble(environment.getProperty(bprefix + ".minbet"));
			this.maxBet = Double.parseDouble(environment.getProperty(bprefix + ".maxbet"));
		}
	}

	String name;

	String UUID;

	String type;

	Double prize;

	Double probability;

	Double minBet;

	Double maxBet;

	public BaseGame(String prefix) {
		bprefix = prefix;
		init();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUUID() {
		return UUID;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Double getPrize() {
		return prize;
	}

	@Override
	public Double getProbability() {
		return probability;
	}

	@Override
	public Double getMinBet() {
		return minBet;
	}

	@Override
	public Double getMaxBet() {
		return maxBet;
	}

	@Override
	public String toString() {
		return name + " [name=" + name + ", UUID=" + UUID + ", type=" + type + ", prize=" + prize + ", probability="
				+ probability + ", minBet=" + minBet + ", maxBet=" + maxBet + "]";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPrize(Double prize) {
		this.prize = prize;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}

	public void setMinBet(Double minBet) {
		this.minBet = minBet;
	}

	public void setMaxBet(Double maxBet) {
		this.maxBet = maxBet;
	}

}
