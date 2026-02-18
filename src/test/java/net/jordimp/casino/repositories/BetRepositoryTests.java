package net.jordimp.casino.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import net.jordimp.casino.services.dto.Bet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BetRepositoryTests {

  @Autowired private BetRepository betRepository;

  @BeforeEach
  void setup() {
    betRepository.deleteAll();
  }

  @Test
  void testSaveAndFindBet() {
    Bet bet = new Bet(10.0, "player-123", "game-456", 100.0);
    bet.setBetUUID("test-bet");

    betRepository.save(bet);

    Bet found = betRepository.findById("test-bet").orElse(null);
    assertNotNull(found);
    assertEquals(10.0, found.getBetAmount());
  }

  @Test
  void testFindByPlayerUUID() {
    Bet bet1 = new Bet(10.0, "player-123", "game-1", 100.0);
    bet1.setBetUUID("bet-1");
    betRepository.save(bet1);

    Bet bet2 = new Bet(20.0, "player-123", "game-2", 90.0);
    bet2.setBetUUID("bet-2");
    betRepository.save(bet2);

    Bet bet3 = new Bet(15.0, "player-456", "game-3", 85.0);
    bet3.setBetUUID("bet-3");
    betRepository.save(bet3);

    List<Bet> player123Bets = betRepository.findByPlayerUUID("player-123");

    assertEquals(2, player123Bets.size());
  }

  @Test
  void testFindByBadFalse() {
    Bet bet1 = new Bet(10.0, "player-123", "game-1", 100.0);
    bet1.setBetUUID("bet-1");
    bet1.setBad(false);
    betRepository.save(bet1);

    Bet bet2 = new Bet(20.0, "player-123", "game-2", 90.0);
    bet2.setBetUUID("bet-2");
    bet2.setBad(true);
    betRepository.save(bet2);

    List<Bet> goodBets = betRepository.findByBadFalse();

    assertEquals(1, goodBets.size());
    assertEquals("bet-1", goodBets.get(0).getBetUUID());
  }
}
