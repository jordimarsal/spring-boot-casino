package net.jordimp.casino.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import net.jordimp.casino.entity.Player;
import net.jordimp.casino.repositories.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PlayerServiceRepositoryTests {

  @Autowired private PlayerService playerService;

  @Autowired private PlayerRepository playerRepository;

  @BeforeEach
  void setup() {
    playerRepository.deleteAll();
  }

  @Test
  void testFindByUUIDReturnsPlayer() {
    String uuid = "test-uuid-repo-" + System.currentTimeMillis();
    Player player = new Player(new Date(), 1000L, uuid, null);
    playerService.save(player);

    Player found = playerService.findByUUID(uuid);

    assertNotNull(found);
    assertEquals(uuid, found.getUuid());
  }

  @Test
  void testFindByUUIDReturnsNullWhenNotFound() {
    assertThrows(
        net.jordimp.casino.exceptions.PlayerNotFoundException.class,
        () -> playerService.findByUUID("non-existent-" + System.currentTimeMillis()));
  }

  @Test
  void testSaveAndRetrievePlayer() {
    String uuid = "test-save-" + System.currentTimeMillis();
    Player player = new Player(new Date(), 1000L, uuid, null);

    playerService.save(player);

    Player found = playerService.findByUUID(uuid);
    assertNotNull(found);
    assertEquals(1000L, found.getMaxTime());
  }
}
