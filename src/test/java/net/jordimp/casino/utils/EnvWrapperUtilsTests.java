package net.jordimp.casino.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

public class EnvWrapperUtilsTests {

    @Test
    void testConcurrentSetEnvDoesNotCauseRaceCondition() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    Environment mockEnv = new MockEnvironment();
                    EnvWrapperUtils.setEnv(mockEnv);
                    Environment retrieved = EnvWrapperUtils.getEnv();
                    if (retrieved != null) {
                        successCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        // All threads should successfully get non-null environment
        assertEquals(threadCount, successCount.get(),
            "All concurrent setEnv calls should complete successfully");
    }

    @Test
    void testGetEnvReturnsNullWhenNotSet() {
        // Reset to null (may need to add resetEnv method)
        EnvWrapperUtils.resetEnv();
        assertNull(EnvWrapperUtils.getEnv(),
            "Should return null when environment not set");
    }

    @Test
    void testSetEnvAndGetEnv() {
        Environment mockEnv = new MockEnvironment();
        EnvWrapperUtils.setEnv(mockEnv);
        assertSame(mockEnv, EnvWrapperUtils.getEnv(),
            "Should return the same environment instance that was set");
    }

    // Simple mock for testing
    private static class MockEnvironment implements Environment {
        @Override
        public String getProperty(String key) { return "test"; }

        @Override
        public String getProperty(String key, String defaultValue) { return defaultValue; }

        @Override
        public <T> T getProperty(String key, Class<T> targetType) { return null; }

        @Override
        public <T> T getProperty(String key, Class<T> targetType, T defaultValue) { return defaultValue; }

        @Override
        public String getRequiredProperty(String key) { return "test"; }

        @Override
        public <T> T getRequiredProperty(String key, Class<T> targetType) { return null; }

        @Override
        public String resolvePlaceholders(String text) { return text; }

        @Override
        public String resolveRequiredPlaceholders(String text) { return text; }

        @Override
        public boolean acceptsProfiles(String... profiles) { return true; }

        @Override
        public boolean acceptsProfiles(org.springframework.core.env.Profiles profiles) { return true; }

        @Override
        public String[] getActiveProfiles() { return new String[0]; }

        @Override
        public String[] getDefaultProfiles() { return new String[0]; }

        @Override
        public boolean containsProperty(String key) { return true; }
    }
}
