package net.jordimp.casino;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import net.jordimp.casino.services.vo.Roulette;
import net.jordimp.casino.utils.EnvWrapperUtils;

public class BaseGameTests {

    @BeforeEach
    void setUp() {
        // Clear the static environment before each test
        EnvWrapperUtils.setEnv(null);
    }

    @Test
    void testInitWithNullEnvironmentThrowsException() {
        // Create a game instance that will fail during construction
        // since env is not autowired in unit test and EnvWrapperUtils.getEnv() returns null
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            new Roulette();
        });

        assertTrue(exception.getMessage().contains("Environment not initialized"));
    }

    @Test
    void testInitWithMissingPropertyUsesDefaultValue() {
        // This test will require mocking Environment
        // For now, we'll document the expected behavior
        // When properties are missing, defaults should be used
    }
}
