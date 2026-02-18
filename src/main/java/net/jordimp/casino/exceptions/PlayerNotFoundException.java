package net.jordimp.casino.exceptions;

public class PlayerNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public PlayerNotFoundException(String uuid) {
    super("Player not found: " + uuid);
  }

  public PlayerNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
