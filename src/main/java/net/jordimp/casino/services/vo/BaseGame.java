package net.jordimp.casino.services.vo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import net.jordimp.casino.utils.CasinoLoggerUtils;
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

			if (environment == null) {
				throw new IllegalStateException("Environment not initialized for game: " + bprefix);
			}

			// Use defaults for missing properties to prevent NPE
			this.name = environment.getProperty(bprefix + ".name", "Unknown Game");
			this.uuid = environment.getProperty(bprefix + ".uuid", "unknown-uuid");
			this.type = environment.getProperty(bprefix + ".type", "Unknown");

			// Safe parsing with defaults
			String prizeStr = environment.getProperty(bprefix + ".prize", "0");
			this.prize = parseSafely(prizeStr, 0.0);

			String probStr = environment.getProperty(bprefix + ".prob", "0");
			this.probability = parseSafely(probStr, 0.0);

			String minBetStr = environment.getProperty(bprefix + ".minbet", "0");
			this.minBet = parseSafely(minBetStr, 0.0);

			String maxBetStr = environment.getProperty(bprefix + ".maxbet", "0");
			this.maxBet = parseSafely(maxBetStr, 0.0);
		}
	}

	/**
	 * Safely parse a string to double with fallback value.
	 *
	 * @param value the string to parse
	 * @param defaultValue the default value if parsing fails
	 * @return the parsed double or default value
	 */
	private double parseSafely(String value, double defaultValue) {
		if (value == null || value.trim().isEmpty()) {
			return defaultValue;
		}
		try {
			return Double.parseDouble(value.trim());
		} catch (NumberFormatException e) {
			CasinoLoggerUtils.warn("BASEGAME",
				"Failed to parse value '" + value + "', using default: " + defaultValue);
			return defaultValue;
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
