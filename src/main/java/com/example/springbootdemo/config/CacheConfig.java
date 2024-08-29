package com.example.springbootdemo.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager concurrentMapCacheManager() {
        return new ConcurrentMapCacheManager(
                "users",
                "products",
                "orders"
        );
    }

    @Bean
    @Primary
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(
                        RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader())
                                .entryTtl(Duration.ofMinutes(10))
                                .disableCachingNullValues()
                )
                .withInitialCacheConfigurations(
                        Map.of(
                                "users",
                                RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader())
                                        .entryTtl(Duration.ofMinutes(10))
                                        .disableCachingNullValues(),
                                "products",
                                RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().getContextClassLoader())
                                        .entryTtl(Duration.ofMinutes(60))
                                        .disableCachingNullValues()
                        )
                )
                .build();
    }
}
