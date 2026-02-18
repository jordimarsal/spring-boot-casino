package net.jordimp.casino.utils;

import org.springframework.core.env.Environment;

public class EnvWrapperUtils {

  private EnvWrapperUtils() {}

  private static volatile Environment environment;

  public static synchronized void setEnv(Environment env) {
    environment = env;
  }

  public static Environment getEnv() {
    return environment;
  }

  // Added for testing purposes
  static synchronized void resetEnv() {
    environment = null;
  }
}
