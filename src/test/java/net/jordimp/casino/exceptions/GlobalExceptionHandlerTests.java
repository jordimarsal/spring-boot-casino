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

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void testPlayerNotFoundExceptionReturns404() {
    // Try to access a non-existent player
    ResponseEntity<ErrorResponse> response =
        restTemplate.getForEntity(
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

  @Test
  void testHandleInsufficientBalanceException() {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();
    InsufficientBalanceException ex =
        new InsufficientBalanceException("Insufficient balance: balance=10, bet=50");

    ErrorResponse response = handler.handleDomainError(ex);

    assertNotNull(response);
    assertEquals("InsufficientBalanceException", response.getError());
    assertTrue(response.getMessage().contains("Insufficient balance"));
  }

  @Test
  void testHandleSessionExpiredException() {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();
    SessionExpiredException ex = new SessionExpiredException("Session expired for player: test-uuid");

    ErrorResponse response = handler.handleDomainError(ex);

    assertNotNull(response);
    assertEquals("SessionExpiredException", response.getError());
    assertTrue(response.getMessage().contains("Session expired"));
  }

  @Test
  void testHandleIllegalArgumentException() {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();
    IllegalArgumentException ex = new IllegalArgumentException("Invalid bet amount: -10");

    ErrorResponse response = handler.handleIllegalArgument(ex);

    assertNotNull(response);
    assertEquals("BadRequest", response.getError());
    assertTrue(response.getMessage().contains("Invalid bet amount"));
  }

  @Test
  void testHandleGenericException() {
    GlobalExceptionHandler handler = new GlobalExceptionHandler();
    Exception ex = new Exception("Unexpected database error");

    ErrorResponse response = handler.handleGenericException(ex);

    assertNotNull(response);
    assertEquals("ServerError", response.getError());
    assertTrue(response.getMessage().contains("Unexpected database error"));
  }
}
