package net.jordimp.casino.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class CasinoLoggerUtilsTests {

  @Test
  void trace_withSingleParameter_doesNotThrowException() {
    assertDoesNotThrow(() -> CasinoLoggerUtils.trace("Trace message"));
  }

  @Test
  void traceWithTagAndMessage_doesNotThrowException() {
    assertDoesNotThrow(() -> CasinoLoggerUtils.trace("TAG", "Trace message with tag"));
  }

  @Test
  void warn_withSingleParameter_doesNotThrowException() {
    assertDoesNotThrow(() -> CasinoLoggerUtils.warn("Warning message"));
  }

  @Test
  void warnWithTagAndMessage_doesNotThrowException() {
    assertDoesNotThrow(() -> CasinoLoggerUtils.warn("TAG", "Warning message with tag"));
  }

  @Test
  void error_withSingleParameter_doesNotThrowException() {
    assertDoesNotThrow(() -> CasinoLoggerUtils.error("Error message"));
  }

  @Test
  void errorWithTagAndMessage_doesNotThrowException() {
    assertDoesNotThrow(() -> CasinoLoggerUtils.error("TAG", "Error message with tag"));
  }

  @Test
  void errorWithTagAndException_doesNotThrowException() {
    Exception ex = new RuntimeException("Test exception");
    assertDoesNotThrow(() -> CasinoLoggerUtils.error("TAG", ex));
  }

  @Test
  void debug_withSingleParameter_doesNotThrowException() {
    assertDoesNotThrow(() -> CasinoLoggerUtils.debug("Debug message"));
  }

  @Test
  void info_withSingleParameter_doesNotThrowException() {
    assertDoesNotThrow(() -> CasinoLoggerUtils.info("Info message"));
  }

  @Test
  void pres_withTagAndMessage_doesNotThrowException() {
    assertDoesNotThrow(() -> CasinoLoggerUtils.pres("TAG", "Message"));
  }

  @Test
  void tres_withMessage_doesNotThrowException() {
    assertDoesNotThrow(() -> CasinoLoggerUtils.tres("Message"));
  }
}
