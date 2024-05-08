package id.ac.ui.cs.advprog.heymartbeproduct.config;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AsyncConfigTest {

    @Test
    void testTaskExecutor() {
        AsyncConfig config = new AsyncConfig();
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) config.taskExecutor();

        assertEquals(5, executor.getCorePoolSize());
        assertEquals(10, executor.getMaxPoolSize());
        assertEquals(120, executor.getQueueCapacity());
        assertEquals("Thread-", executor.getThreadNamePrefix());
    }
}