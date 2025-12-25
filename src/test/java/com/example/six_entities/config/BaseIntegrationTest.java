package com.example.six_entities.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

public abstract class BaseIntegrationTest {

    @Autowired
    protected CacheManager cacheManager;
}
