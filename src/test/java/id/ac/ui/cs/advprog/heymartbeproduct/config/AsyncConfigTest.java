package id.ac.ui.cs.advprog.heymartbeproduct.config;

import org.junit.jupiter.api.Test;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncConfigTest {

    @Test
    void testTaskExecutor() {
        AsyncConfig config = new AsyncConfig();
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) config.taskExecutor();

        assertEquals(4, executor.getCorePoolSize());
        assertEquals(4, executor.getMaxPoolSize());
        assertEquals(150, executor.getQueueCapacity());
        assertEquals("Thread-", executor.getThreadNamePrefix());
    }

    @Test
    void testGetAsyncUncaughtExceptionHandler() {
        AsyncConfig config = new AsyncConfig();
        AsyncUncaughtExceptionHandler handler = config.getAsyncUncaughtExceptionHandler();
        assertTrue(handler instanceof SimpleAsyncUncaughtExceptionHandler);
    }
}