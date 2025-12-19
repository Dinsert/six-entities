package com.example.six_entities.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;

import java.util.Random;

@Configuration
public class UserProfileClientConfig {

    @Bean
    public BackOffPolicy jitterBackOffPolicy() {
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(500);
        policy.setMultiplier(2.0);
        policy.setMaxInterval(5000);

        return new BackOffPolicy() {
            private final Random random = new Random();

            @Override
            public BackOffContext start(org.springframework.retry.RetryContext context) {
                return policy.start(context);
            }

            @Override
            public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
                policy.backOff(backOffContext);
                long jitter = (long) ((random.nextDouble() - 0.5) * 2 * 0.2 * policy.getMaxInterval()); // ±20%
                try {
                    Thread.sleep(jitter);
                } catch (InterruptedException e) {
                    throw new BackOffInterruptedException("Interrupted jitter sleep", e);
                }
            }
        };
    }
}
