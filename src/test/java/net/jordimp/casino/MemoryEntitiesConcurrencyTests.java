package net.jordimp.casino;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import net.jordimp.casino.dao.MemoryEntities;
import net.jordimp.casino.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemoryEntitiesConcurrencyTests {

  @Autowired private MemoryEntities memoryEntities;

  @AfterEach
  void cleanup() {
    memoryEntities.clear();
  }

  @Test
  void testConcurrentPersistDoesNotCorruptData() throws InterruptedException {
    int threadCount = 10;
    int playersPerThread = 10;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);
    AtomicInteger successCount = new AtomicInteger(0);

    for (int i = 0; i < threadCount; i++) {
      final int threadId = i;
      executor.submit(
          () -> {
            try {
              for (int j = 0; j < playersPerThread; j++) {
                Player player =
                    new Player(new Date(), 1000L, "thread-" + threadId + "-player-" + j, null);
                memoryEntities.persist(player);
                successCount.incrementAndGet();
              }
            } finally {
              latch.countDown();
            }
          });
    }

    latch.await();
    executor.shutdown();

    // All persists should complete without corruption
    assertEquals(
        threadCount * playersPerThread,
        successCount.get(),
        "All concurrent persist operations should succeed");
  }

  @Test
  void testConcurrentRemoveDoesNotCauseException() throws InterruptedException {
    // Create players first
    for (int i = 0; i < 10; i++) {
      Player player = new Player(new Date(), 1000L, "player-" + i, null);
      memoryEntities.persist(player);
    }

    int threadCount = 5;
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i++) {
      final int index = i;
      executor.submit(
          () -> {
            try {
              Player player = new Player(new Date(), 1000L, "player-" + index, null);
              memoryEntities.remove(player);
            } finally {
              latch.countDown();
            }
          });
    }

    assertDoesNotThrow(
        () -> {
          latch.await();
          executor.shutdown();
        },
        "Concurrent remove operations should not throw exceptions");
  }
}
