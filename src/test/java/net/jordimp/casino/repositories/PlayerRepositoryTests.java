package net.jordimp.casino.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import net.jordimp.casino.entity.Player;
import net.jordimp.casino.entity.UserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PlayerRepositoryTests {

  @Autowired private PlayerRepository playerRepository;

  @BeforeEach
  void setup() {
    playerRepository.deleteAll();
  }

  @Test
  void testSaveAndFindPlayer() {
    Player player = new Player(new Date(), 1000L, "test-uuid", UserProvider.POKERSTAR);

    playerRepository.save(player);

    Player found = playerRepository.findById("test-uuid").orElse(null);
    assertNotNull(found);
    assertEquals(UserProvider.POKERSTAR, found.getUserProvider());
  }

  @Test
  @Transactional
  void testDeleteExpiredPlayers() {
    // Create expired player (loginDate 10 seconds ago)
    Date expiredDate = new Date(System.currentTimeMillis() - 10000);
    Player expired = new Player(expiredDate, 5000L, "expired", null);
    playerRepository.save(expired);

    // Create active player (loginDate in future)
    Date activeDate = new Date(System.currentTimeMillis() + 10000);
    Player active = new Player(activeDate, 10000L, "active", null);
    playerRepository.save(active);

    // Delete players with loginDate before NOW
    int deleted = playerRepository.deleteExpiredPlayers(new Date());

    assertEquals(1, deleted);
    assertFalse(playerRepository.existsById("expired"));
    assertTrue(playerRepository.existsById("active"));
  }
}
