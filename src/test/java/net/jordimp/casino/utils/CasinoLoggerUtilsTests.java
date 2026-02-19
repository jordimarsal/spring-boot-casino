package net.jordimp.casino.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;

/**
 * Test class for CasinoLoggerUtils.
 * Tests both enabled and disabled logging states to achieve full branch coverage.
 */
class CasinoLoggerUtilsTests {

  private Appender<ILoggingEvent> mockAppender;
  private Logger logger;

  @BeforeEach
  void setUp() {
    // Get the underlying Logback logger
    logger = (Logger) LoggerFactory.getLogger(CasinoLoggerUtils.class);

    // Create and attach mock appender
    mockAppender = mock(Appender.class);
    logger.addAppender(mockAppender);
  }

  @AfterEach
  void tearDown() {
    // Detach mock appender and reset logger level
    logger.detachAppender(mockAppender);
    logger.setLevel(null); // Reset to default
  }

  @Test
  void trace_withSingleParameter_whenDisabled_doesNotThrowException() {
    // Arrange - Disable trace level
    logger.setLevel(Level.INFO); // Set level higher than TRACE

    // Act & Assert - Should not throw even when disabled
    assertDoesNotThrow(() -> CasinoLoggerUtils.trace("Trace message"));

    // Verify that logging was skipped (trace level is disabled)
    verify(mockAppender, never()).doAppend(any());
  }

  @Test
  void trace_withSingleParameter_whenEnabled_logsMessage() {
    // Arrange - Enable trace level
    logger.setLevel(Level.TRACE);

    // Act
    CasinoLoggerUtils.trace("Trace message");

    // Assert - Verify logging occurred
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.TRACE)
            && event.getFormattedMessage().equals("Trace message");
      }
    }));
  }

  @Test
  void traceWithTagAndMessage_whenDisabled_doesNotThrowException() {
    // Arrange - Disable trace level
    logger.setLevel(Level.INFO);

    // Act & Assert
    assertDoesNotThrow(() -> CasinoLoggerUtils.trace("TAG", "Trace message with tag"));

    // Verify that logging was skipped
    verify(mockAppender, never()).doAppend(any());
  }

  @Test
  void traceWithTagAndMessage_whenEnabled_logsMessageWithTag() {
    // Arrange - Enable trace level
    logger.setLevel(Level.TRACE);

    // Act
    CasinoLoggerUtils.trace("TAG", "Trace message with tag");

    // Assert - Verify logging occurred with concatenated tag and message
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.TRACE)
            && event.getFormattedMessage().equals("TAG -> Trace message with tag");
      }
    }));
  }

  @Test
  void debug_withSingleParameter_whenDisabled_doesNotThrowException() {
    // Arrange - Disable debug level
    logger.setLevel(Level.INFO);

    // Act & Assert
    assertDoesNotThrow(() -> CasinoLoggerUtils.debug("Debug message"));

    // Verify that logging was skipped
    verify(mockAppender, never()).doAppend(any());
  }

  @Test
  void debug_withSingleParameter_whenEnabled_logsMessage() {
    // Arrange - Enable debug level
    logger.setLevel(Level.DEBUG);

    // Act
    CasinoLoggerUtils.debug("Debug message");

    // Assert
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.DEBUG)
            && event.getFormattedMessage().equals("Debug message");
      }
    }));
  }

  @Test
  void debugWithTagAndMessage_whenDisabled_doesNotThrowException() {
    // Arrange - Disable debug level
    logger.setLevel(Level.INFO);

    // Act & Assert
    assertDoesNotThrow(() -> CasinoLoggerUtils.debug("TAG", "Debug message with tag"));

    // Verify that logging was skipped
    verify(mockAppender, never()).doAppend(any());
  }

  @Test
  void debugWithTagAndMessage_whenEnabled_logsMessageWithTag() {
    // Arrange - Enable debug level
    logger.setLevel(Level.DEBUG);

    // Act
    CasinoLoggerUtils.debug("TAG", "Debug message with tag");

    // Assert
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.DEBUG)
            && event.getFormattedMessage().equals("TAG -> Debug message with tag");
      }
    }));
  }

  @Test
  void info_withSingleParameter_whenDisabled_doesNotThrowException() {
    // Arrange - Disable info level
    logger.setLevel(Level.WARN);

    // Act & Assert
    assertDoesNotThrow(() -> CasinoLoggerUtils.info("Info message"));

    // Verify that logging was skipped
    verify(mockAppender, never()).doAppend(any());
  }

  @Test
  void info_withSingleParameter_whenEnabled_logsMessage() {
    // Arrange - Enable info level
    logger.setLevel(Level.INFO);

    // Act
    CasinoLoggerUtils.info("Info message");

    // Assert
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.INFO)
            && event.getFormattedMessage().equals("Info message");
      }
    }));
  }

  @Test
  void infoWithTagAndMessage_whenDisabled_doesNotThrowException() {
    // Arrange - Disable info level
    logger.setLevel(Level.WARN);

    // Act & Assert
    assertDoesNotThrow(() -> CasinoLoggerUtils.info("TAG", "Info message with tag"));

    // Verify that logging was skipped
    verify(mockAppender, never()).doAppend(any());
  }

  @Test
  void infoWithTagAndMessage_whenEnabled_logsMessageWithTag() {
    // Arrange - Enable info level
    logger.setLevel(Level.INFO);

    // Act
    CasinoLoggerUtils.info("TAG", "Info message with tag");

    // Assert
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.INFO)
            && event.getFormattedMessage().equals("TAG -> Info message with tag");
      }
    }));
  }

  @Test
  void warn_withSingleParameter_whenDisabled_doesNotThrowException() {
    // Arrange - Disable warn level
    logger.setLevel(Level.ERROR);

    // Act & Assert
    assertDoesNotThrow(() -> CasinoLoggerUtils.warn("Warning message"));

    // Verify that logging was skipped
    verify(mockAppender, never()).doAppend(any());
  }

  @Test
  void warn_withSingleParameter_whenEnabled_logsMessage() {
    // Arrange - Enable warn level
    logger.setLevel(Level.WARN);

    // Act
    CasinoLoggerUtils.warn("Warning message");

    // Assert
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.WARN)
            && event.getFormattedMessage().equals("Warning message");
      }
    }));
  }

  @Test
  void warnWithTagAndMessage_whenDisabled_doesNotThrowException() {
    // Arrange - Disable warn level
    logger.setLevel(Level.ERROR);

    // Act & Assert
    assertDoesNotThrow(() -> CasinoLoggerUtils.warn("TAG", "Warning message with tag"));

    // Verify that logging was skipped
    verify(mockAppender, never()).doAppend(any());
  }

  @Test
  void warnWithTagAndMessage_whenEnabled_logsMessageWithTag() {
    // Arrange - Enable warn level
    logger.setLevel(Level.WARN);

    // Act
    CasinoLoggerUtils.warn("TAG", "Warning message with tag");

    // Assert
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.WARN)
            && event.getFormattedMessage().equals("TAG -> Warning message with tag");
      }
    }));
  }

  @Test
  void error_withSingleParameter_whenDisabled_doesNotThrowException() {
    // Arrange - Disable error level
    logger.setLevel(Level.OFF);

    // Act & Assert
    assertDoesNotThrow(() -> CasinoLoggerUtils.error("Error message"));

    // Verify that logging was skipped
    verify(mockAppender, never()).doAppend(any());
  }

  @Test
  void error_withSingleParameter_whenEnabled_logsMessage() {
    // Arrange - Enable error level
    logger.setLevel(Level.ERROR);

    // Act
    CasinoLoggerUtils.error("Error message");

    // Assert
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.ERROR)
            && event.getFormattedMessage().equals("Error message");
      }
    }));
  }

  @Test
  void errorWithTagAndMessage_whenDisabled_doesNotThrowException() {
    // Arrange - Disable error level
    logger.setLevel(Level.OFF);

    // Act & Assert
    assertDoesNotThrow(() -> CasinoLoggerUtils.error("TAG", "Error message with tag"));

    // Verify that logging was skipped
    verify(mockAppender, never()).doAppend(any());
  }

  @Test
  void errorWithTagAndMessage_whenEnabled_logsMessageWithTag() {
    // Arrange - Enable error level
    logger.setLevel(Level.ERROR);

    // Act
    CasinoLoggerUtils.error("TAG", "Error message with tag");

    // Assert
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.ERROR)
            && event.getFormattedMessage().equals("TAG -> Error message with tag");
      }
    }));
  }

  @Test
  void errorWithTagAndException_whenEnabled_logsExceptionMessage() {
    // Arrange - Enable error level
    logger.setLevel(Level.ERROR);
    Exception ex = new RuntimeException("Test exception");

    // Act
    CasinoLoggerUtils.error("TAG", ex);

    // Assert
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.ERROR)
            && event.getFormattedMessage().equals("TAG -> Test exception");
      }
    }));
  }

  @Test
  void pres_withTagAndMessage_whenDisabled_doesNotThrowException() {
    // Arrange - Disable info level (pres uses info level)
    logger.setLevel(Level.WARN);

    // Act & Assert
    assertDoesNotThrow(() -> CasinoLoggerUtils.pres("TAG", "Message"));

    // Verify that logging was skipped
    verify(mockAppender, never()).doAppend(any());
  }

  @Test
  void pres_withTagAndMessage_whenEnabled_logsFormattedMessage() {
    // Arrange - Enable info level
    logger.setLevel(Level.INFO);

    // Act
    CasinoLoggerUtils.pres("TAG", "Message");

    // Assert
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.INFO)
            && event.getFormattedMessage().equals("## TAG Message ##");
      }
    }));
  }

  @Test
  void tres_withMessage_whenDisabled_doesNotThrowException() {
    // Arrange - Disable info level (tres uses info level)
    logger.setLevel(Level.WARN);

    // Act & Assert
    assertDoesNotThrow(() -> CasinoLoggerUtils.tres("Message"));

    // Verify that logging was skipped
    verify(mockAppender, never()).doAppend(any());
  }

  @Test
  void tres_withMessage_whenEnabled_logsFormattedMessage() {
    // Arrange - Enable info level
    logger.setLevel(Level.INFO);

    // Act
    CasinoLoggerUtils.tres("Message");

    // Assert
    verify(mockAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
      @Override
      public boolean matches(ILoggingEvent event) {
        return event.getLevel().equals(Level.INFO)
            && event.getFormattedMessage().equals("     Message");
      }
    }));
  }
}
