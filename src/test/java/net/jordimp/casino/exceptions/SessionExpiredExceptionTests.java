package net.jordimp.casino.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class SessionExpiredExceptionTests {

  @Test
  void exceptionConstructor_createsExceptionWithMessage() {
    String expectedMessage = "Session expired for player";

    SessionExpiredException exception = new SessionExpiredException(expectedMessage);

    assertNotNull(exception);
    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void exceptionConstructor_createsExceptionWithNullMessage() {
    SessionExpiredException exception = new SessionExpiredException(null);

    assertNotNull(exception);
  }
}
