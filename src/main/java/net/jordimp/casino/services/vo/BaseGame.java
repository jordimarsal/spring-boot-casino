package net.jordimp.casino.services.vo;

import jakarta.annotation.PostConstruct;
import net.jordimp.casino.utils.CasinoLoggerUtils;
import net.jordimp.casino.utils.EnvWrapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public abstract class BaseGame implements Game {

  @Autowired private Environment env;

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
      CasinoLoggerUtils.warn(
          "BASEGAME", "Failed to parse value '" + value + "', using default: " + defaultValue);
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

/**
	 * Ensure this game's properties are initialized from the Environment if they
	 * were not populated earlier (for example when the game instance was created
	 * before EnvWrapperUtils was populated). This makes games resilient to
	 * different bean initialization orders in tests and startup.
	 */
	private void ensureInitializedFromEnvIfNeeded() {
		if (bprefix == null) {
			return;
		}
		// If already populated, nothing to do
		if (name != null && minBet != null && maxBet != null) {
			return;
		}
		org.springframework.core.env.Environment environment = EnvWrapperUtils.getEnv();
		if (environment == null) {
			// environment not available yet; leave defaults in place for now
			return;
		}

		CasinoLoggerUtils.debug("BASEGAME", "Initializing game properties from Environment for prefix=" + bprefix);
		this.name = environment.getProperty(bprefix + ".name", this.name == null ? "Unknown Game" : this.name);
		this.uuid = environment.getProperty(bprefix + ".uuid", this.uuid == null ? "unknown-uuid" : this.uuid);
		this.type = environment.getProperty(bprefix + ".type", this.type == null ? "Unknown" : this.type);

		this.prize = parseSafely(environment.getProperty(bprefix + ".prize", String.valueOf(this.prize == null ? 0 : this.prize)), 0.0);
		this.probability = parseSafely(environment.getProperty(bprefix + ".prob", String.valueOf(this.probability == null ? 0 : this.probability)), 0.0);
		this.minBet = parseSafely(environment.getProperty(bprefix + ".minbet", String.valueOf(this.minBet == null ? 0 : this.minBet)), 0.0);
		this.maxBet = parseSafely(environment.getProperty(bprefix + ".maxbet", String.valueOf(this.maxBet == null ? 0 : this.maxBet)), 0.0);
	}

	@Override
	public Double getMinBet() {
		ensureInitializedFromEnvIfNeeded();
		return minBet;
	}

	@Override
	public Double getMaxBet() {
		ensureInitializedFromEnvIfNeeded();
    return maxBet;
  }

  @Override
  public String toString() {
    return name
        + " [name="
        + name
        + ", UUID="
        + uuid
        + ", type="
        + type
        + ", prize="
        + prize
        + ", probability="
        + probability
        + ", minBet="
        + minBet
        + ", maxBet="
        + maxBet
        + "]";
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
