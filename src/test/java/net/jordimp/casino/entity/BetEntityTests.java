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
public class BetEntityTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testBetCanBePersisted() {
        net.jordimp.casino.services.dto.Bet bet =
            new net.jordimp.casino.services.dto.Bet(10.0, "player-123", "game-456", 100.0);
        bet.setBetUUID("test-bet-uuid");

        entityManager.persist(bet);
        entityManager.flush();

        assertNotNull(bet.getBetUUID());
        assertEquals("test-bet-uuid", bet.getBetUUID());
    }

    @Test
    void testBetCanBeRetrieved() {
        net.jordimp.casino.services.dto.Bet bet =
            new net.jordimp.casino.services.dto.Bet(10.0, "player-123", "game-456", 100.0);
        bet.setBetUUID("test-bet-retrieve");

        entityManager.persist(bet);
        entityManager.flush();
        entityManager.clear();

        net.jordimp.casino.services.dto.Bet found = entityManager.find(
            net.jordimp.casino.services.dto.Bet.class, "test-bet-retrieve");

        assertNotNull(found);
        assertEquals("test-bet-retrieve", found.getBetUUID());
        assertEquals(10.0, found.getBetAmount());
    }

    @Test
    void testBetCalculateWin() {
        net.jordimp.casino.services.dto.Bet bet =
            new net.jordimp.casino.services.dto.Bet(10.0, "player-123", "game-456", 100.0);
        bet.setBad(true);

        bet.calculateWin(50.0);

        assertEquals(50.0, bet.getPrizeAmount());
        assertFalse(bet.isBad());
    }

    @Test
    void testBetCalculateWinWithZeroPrize() {
        net.jordimp.casino.services.dto.Bet bet =
            new net.jordimp.casino.services.dto.Bet(10.0, "player-123", "game-456", 100.0);
        bet.setBad(true);

        bet.calculateWin(0.0);

        // Should not update with zero or negative prize
        assertNull(bet.getPrizeAmount());
    }

    @Test
    void testBetNetBalanceChange() {
        net.jordimp.casino.services.dto.Bet bet =
            new net.jordimp.casino.services.dto.Bet(10.0, "player-123", "game-456", 100.0);
        bet.calculateWin(50.0);

        assertEquals(40.0, bet.getNetBalanceChange());
    }

    @Test
    void testBetNetBalanceChangeWithNoPrize() {
        net.jordimp.casino.services.dto.Bet bet =
            new net.jordimp.casino.services.dto.Bet(10.0, "player-123", "game-456", 100.0);

        assertEquals(-10.0, bet.getNetBalanceChange());
    }
}
