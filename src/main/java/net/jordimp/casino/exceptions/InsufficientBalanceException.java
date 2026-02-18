package net.jordimp.casino.exceptions;

public class InsufficientBalanceException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public InsufficientBalanceException(String message) {
    super(message);
  }
}
