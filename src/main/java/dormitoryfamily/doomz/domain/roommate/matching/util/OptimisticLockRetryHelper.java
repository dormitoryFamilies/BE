package dormitoryfamily.doomz.domain.roommate.matching.util;

import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class OptimisticLockRetryHelper {

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF_MS = 50L;

    public static <T> T executeWithRetry(Supplier<T> operation) {
        int attempt = 0;

        while (true) {
            try {
                return operation.get();
            } catch (OptimisticLockException e) {
                attempt++;

                if (attempt >= MAX_RETRIES) {
                    log.error("OptimisticLockException occurred after {} retries", MAX_RETRIES);
                    throw e;
                }

                long backoffTime = calculateBackoffTime(attempt);
                log.warn("OptimisticLockException occurred. Retrying attempt {}/{} after {}ms",
                        attempt, MAX_RETRIES, backoffTime);

                try {
                    Thread.sleep(backoffTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }
    }

    public static void executeWithRetry(Runnable operation) {
        executeWithRetry(() -> {
            operation.run();
            return null;
        });
    }

    private static long calculateBackoffTime(int attempt) {
        return INITIAL_BACKOFF_MS * (1L << (attempt - 1));
    }
}
