package net.jordimp.casino.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GlobalExceptionHandlerTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void testPlayerNotFoundExceptionReturns404() {
		// Try to access a non-existent player
		ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
				"/api/casino/get/non-existent-player-" + System.currentTimeMillis(),
				ErrorResponse.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("PlayerNotFound", response.getBody().getError());
	}

	@Test
	void testHandleExceptionCreatesErrorResponse() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		PlayerNotFoundException ex = new PlayerNotFoundException("test-uuid");

		ErrorResponse response = handler.handlePlayerNotFound(ex);

		assertNotNull(response);
		assertEquals("PlayerNotFound", response.getError());
		assertTrue(response.getMessage().contains("test-uuid"));
	}
}
