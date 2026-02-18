package net.jordimp.casino.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class InsufficientBalanceExceptionTests {

  @Test
  void exceptionConstructor_createsExceptionWithMessage() {
    String expectedMessage = "Insufficient balance for bet";

    InsufficientBalanceException exception = new InsufficientBalanceException(expectedMessage);

    assertNotNull(exception);
    assertEquals(expectedMessage, exception.getMessage());
  }

  @Test
  void exceptionConstructor_createsExceptionWithNullMessage() {
    InsufficientBalanceException exception = new InsufficientBalanceException(null);

    assertNotNull(exception);
  }
}
