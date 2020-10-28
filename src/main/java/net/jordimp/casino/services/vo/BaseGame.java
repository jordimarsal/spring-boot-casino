package net.jordimp.casino.services.vo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import net.jordimp.casino.utils.EnvWrapperUtils;

public abstract class BaseGame implements Game {

	@Autowired
	private Environment env;

	protected String bprefix;

	@PostConstruct
	public void init() {
		if (env != null) {
			EnvWrapperUtils.setEnv(env);
		}
		if (bprefix != null) {
			Environment environment = EnvWrapperUtils.getEnv();
			this.name = environment.getProperty(bprefix + ".name");
			this.uuid = environment.getProperty(bprefix + ".uuid");
			this.type = environment.getProperty(bprefix + ".type");
			this.prize = Double.parseDouble(environment.getProperty(bprefix + ".prize"));
			this.probability = Double.parseDouble(environment.getProperty(bprefix + ".prob"));
			this.minBet = Double.parseDouble(environment.getProperty(bprefix + ".minbet"));
			this.maxBet = Double.parseDouble(environment.getProperty(bprefix + ".maxbet"));
		}
	}

	String name;

	String uuid;

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
		return uuid;
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
		return name + " [name=" + name + ", UUID=" + uuid + ", type=" + type + ", prize=" + prize + ", probability="
				+ probability + ", minBet=" + minBet + ", maxBet=" + maxBet + "]";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUUID(String uUID) {
		uuid = uUID;
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
