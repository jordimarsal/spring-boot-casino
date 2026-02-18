package net.jordimp.casino.e2e;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.jordimp.casino.entity.Player;
import net.jordimp.casino.repositories.PlayerRepository;
import net.jordimp.casino.services.dto.Bet;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BetE2ETests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private PlayerRepository playerRepository;

	@Test
	@Order(1)
	void testCompleteBetFlow() {
		// 1. Login player
		String playerUuid = "e2e-player-" + System.currentTimeMillis();

		Player player = new Player(new Date(), 1000L, playerUuid, null);
		ResponseEntity<Player> loginResponse = restTemplate.postForEntity(
				"/api/casino/logon",
				player,
				Player.class);

		assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

		// 2. Verify player persisted
		assertTrue(playerRepository.existsById(playerUuid));

		// 3. Place bet
		Bet bet = new Bet(10.0, playerUuid, "VIDEOBINGO-UUID", 100.0);
		ResponseEntity<Bet> betResponse = restTemplate.postForEntity(
				"/api/casino/bet/" + playerUuid,
				bet,
				Bet.class);

		assertEquals(HttpStatus.OK, betResponse.getStatusCode());

		// 4. Verify response body exists
		assertNotNull(betResponse.getBody());
	}

	@Test
	@Order(2)
	void testPlayerNotFoundFlow() {
		String nonExistentUuid = "non-existent-" + System.currentTimeMillis();

		ResponseEntity<String> response = restTemplate.getForEntity(
				"/api/casino/get/" + nonExistentUuid,
				String.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	@Order(3)
	void testLoginAndLogoutFlow() {
		String playerUuid = "e2e-logout-" + System.currentTimeMillis();

		// Login
		Player player = new Player(new Date(), 1000L, playerUuid, null);
		ResponseEntity<String> loginResponse = restTemplate.postForEntity(
				"/api/casino/logon",
				player,
				String.class);

		assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
		assertNotNull(loginResponse.getBody());

		// Logout
		ResponseEntity<String> logoutResponse = restTemplate.postForEntity(
				"/api/casino/logout/" + playerUuid,
				null,
				String.class);

		assertEquals(HttpStatus.OK, logoutResponse.getStatusCode());
		assertNotNull(logoutResponse.getBody());
	}
}
