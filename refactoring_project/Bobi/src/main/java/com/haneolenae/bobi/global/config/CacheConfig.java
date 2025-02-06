package com.haneolenae.bobi.global.config;


import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import java.io.IOException;


@Configuration
@EnableCaching
public class CacheConfig {

    @Bean(name = "ehCacheManager")
    public org.springframework.cache.CacheManager cacheManager() throws IOException {
        CachingProvider cachingProvider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
        CacheManager manager = cachingProvider.getCacheManager(
                new ClassPathResource("/ehcache.xml").getURI(),
                getClass().getClassLoader());

        return new JCacheCacheManager(manager);
    }
}
