package net.jordimp.casino;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import net.jordimp.casino.entity.Player;
import net.jordimp.casino.entity.UserProvider;
import net.jordimp.casino.services.dto.Bet;

public class TestUtils {

  public static String asJsonString(final Object obj) throws Exception {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /**
   * Creates a valid Bet object for testing.
   *
   * @return a Bet with valid default values
   */
  public static Bet createValidBet() {
    Bet bet = new Bet(10.0, "player-uuid-123", "ROULETTE-UUID", 100.0);
    bet.setBetUUID("test-bet-uuid");
    return bet;
  }

  /**
   * Creates a valid Bet object with a specific bet amount.
   *
   * @param amount the bet amount
   * @return a Bet with the specified amount
   */
  public static Bet createValidBet(Double amount) {
    Bet bet = new Bet(amount, "player-uuid-123", "ROULETTE-UUID", 100.0);
    bet.setBetUUID("test-bet-uuid");
    return bet;
  }

  /**
   * Creates a valid Bet object with a specific game UUID.
   * This allows testing with different game instances to avoid caching issues.
   *
   * @param gameUUID the game UUID to use
   * @return a Bet with the specified game UUID
   */
  public static Bet createValidBetWithGameUUID(String gameUUID) {
    Bet bet = new Bet(10.0, "player-uuid-123", gameUUID, 100.0);
    bet.setBetUUID("test-bet-uuid-" + gameUUID);
    return bet;
  }

  /**
   * Creates a valid Player object for testing.
   *
   * @return a Player with valid default values and recent login date
   */
  public static Player createValidPlayer() {
    Player player = new Player();
    player.setUUID("player-uuid-123");
    player.setUserProvider(UserProvider.OTHER);
    // Set login date to now (within session time)
    player.setLoginDate(new Date());
    // Set maxTime to 1 hour (3600 seconds)
    player.setMaxTime(3600L);
    return player;
  }

  /**
   * Creates a valid Player object with a specific login date.
   *
   * @param loginDate the login date for the player
   * @return a Player with the specified login date
   */
  public static Player createValidPlayer(Date loginDate) {
    Player player = new Player();
    player.setUUID("player-uuid-123");
    player.setUserProvider(UserProvider.OTHER);
    player.setLoginDate(loginDate);
    // Set maxTime to 1 hour (3600 seconds)
    player.setMaxTime(3600L);
    return player;
  }
}
