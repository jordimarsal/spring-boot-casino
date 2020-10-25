package net.jordimp.casino.services.vo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.core.env.Environment;

@ConstructorBinding
@ConfigurationProperties(prefix = "bj")
public class Blackjack implements Game {

	@Autowired
	private Environment env;

	private static Environment environment;

	@PostConstruct
	public void init() {
		if (env != null) {
			environment = env;
		}
		if (environment != null) {
			this.name = environment.getProperty("bj.name");
			this.UUID = environment.getProperty("bj.uuid");
			this.type = environment.getProperty("bj.type");
			this.prize = Double.parseDouble(environment.getProperty("bj.prize"));
			this.probability = Double.parseDouble(environment.getProperty("bj.prob"));
			this.minBet = Double.parseDouble(environment.getProperty("bj.minbet"));
			this.maxBet = Double.parseDouble(environment.getProperty("bj.maxbet"));
		}
	}

	String name;

	String UUID;

	String type;

	Double prize;

	Double probability;

	Double minBet;

	Double maxBet;

	public Blackjack() {
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
		return "Blackjack [name=" + name + ", UUID=" + UUID + ", type=" + type + ", prize=" + prize + ", probability="
				+ probability + ", minBet=" + minBet + ", maxBet=" + maxBet + "]";
	}

	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
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
