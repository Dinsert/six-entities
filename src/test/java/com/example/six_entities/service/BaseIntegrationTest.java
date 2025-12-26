package com.example.six_entities.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

abstract class BaseIntegrationTest {

    @Autowired
    protected CacheManager cacheManager;
}
