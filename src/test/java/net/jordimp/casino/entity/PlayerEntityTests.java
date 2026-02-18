package net.jordimp.casino.entity;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class PlayerEntityTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testPlayerCanBePersisted() {
        Player player = new Player(new java.util.Date(), 1000L, "test-uuid-persist", null);

        entityManager.persist(player);
        entityManager.flush();

        assertNotNull(player.getUuid());
        assertEquals("test-uuid-persist", player.getUuid());
    }

    @Test
    void testPlayerCanBeRetrieved() {
        Player player = new Player(new java.util.Date(), 1000L, "test-uuid-retrieve", null);

        entityManager.persist(player);
        entityManager.flush();
        entityManager.clear();

        Player found = entityManager.find(Player.class, "test-uuid-retrieve");

        assertNotNull(found);
        assertEquals("test-uuid-retrieve", found.getUuid());
    }

    @Test
    void testPlayerCanPlaceBet() {
        Player player = new Player();
        player.setLoginDate(new java.util.Date());
        player.setMaxTime(10000L);

        assertTrue(player.canPlaceBet(10.0));
    }

    @Test
    void testExpiredPlayerCannotPlaceBet() {
        Player player = new Player();
        player.setLoginDate(new java.util.Date(System.currentTimeMillis() - 20000));
        player.setMaxTime(10000L);

        assertFalse(player.canPlaceBet(10.0));
    }

    @Test
    void testPlayerWithNoLoginCannotPlaceBet() {
        Player player = new Player();
        player.setLoginDate(null);
        player.setMaxTime(10000L);

        assertFalse(player.canPlaceBet(10.0));
    }
}
