package com.xinput.baseboot.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.List;

@Configuration
@EnableCaching
public class LocalCaffeineCache {

  private static final Logger logger = LoggerFactory.getLogger(LocalCaffeineCache.class);

  // ===== 1s =====
  public static final String CACHE_1s = "cache_1s";
  public static final String CACHE_2_1s = "cache_2_1s";
  public static final String CACHE_3_1s = "cache_3_1s";
  public static final String CACHE_4_1s = "cache_4_1s";
  public static final String CACHE_5_1s = "cache_5_1s";
  public static final String CACHE_6_1s = "cache_6_1s";
  public static final String CACHE_7_1s = "cache_7_1s";
  public static final String CACHE_8_1s = "cache_8_1s";
  public static final String CACHE_9_1s = "cache_9_1s";
  public static final String CACHE_10_1s = "cache_10_1s";

  // ===== 2s =====
  public static final String CACHE_2s = "cache_2s";
  public static final String CACHE_2_2s = "cache_2_2s";
  public static final String CACHE_3_2s = "cache_3_2s";
  public static final String CACHE_4_2s = "cache_4_2s";
  public static final String CACHE_5_2s = "cache_5_2s";
  public static final String CACHE_6_2s = "cache_6_2s";
  public static final String CACHE_7_2s = "cache_7_2s";
  public static final String CACHE_8_2s = "cache_8_2s";
  public static final String CACHE_9_2s = "cache_9_2s";
  public static final String CACHE_10_2s = "cache_10_2s";

  // ===== 3s =====
  public static final String CACHE_3s = "cache_3s";
  public static final String CACHE_2_3s = "cache_2_3s";
  public static final String CACHE_3_3s = "cache_3_3s";
  public static final String CACHE_4_3s = "cache_4_3s";
  public static final String CACHE_5_3s = "cache_5_3s";
  public static final String CACHE_6_3s = "cache_6_3s";
  public static final String CACHE_7_3s = "cache_7_3s";
  public static final String CACHE_8_3s = "cache_8_3s";
  public static final String CACHE_9_3s = "cache_9_3s";
  public static final String CACHE_10_3s = "cache_10_3s";

  // ===== 5s =====
  public static final String CACHE_5s = "cache_5s";
  public static final String CACHE_2_5s = "cache_2_5s";
  public static final String CACHE_3_5s = "cache_3_5s";
  public static final String CACHE_4_5s = "cache_4_5s";
  public static final String CACHE_5_5s = "cache_5_5s";
  public static final String CACHE_6_5s = "cache_6_5s";
  public static final String CACHE_7_5s = "cache_7_5s";
  public static final String CACHE_8_5s = "cache_8_5s";
  public static final String CACHE_9_5s = "cache_9_5s";
  public static final String CACHE_10_5s = "cache_10_5s";

  // ===== 10s =====
  public static final String CACHE_10s = "cache_10s";
  public static final String CACHE_2_10s = "cache_2_10s";
  public static final String CACHE_3_10s = "cache_3_10s";
  public static final String CACHE_4_10s = "cache_4_10s";
  public static final String CACHE_5_10s = "cache_5_10s";
  public static final String CACHE_6_10s = "cache_6_10s";
  public static final String CACHE_7_10s = "cache_7_10s";
  public static final String CACHE_8_10s = "cache_8_10s";
  public static final String CACHE_9_10s = "cache_9_10s";
  public static final String CACHE_10_10s = "cache_10_10s";

  // ===== 15s =====
  public static final String CACHE_15s = "cache_15s";
  public static final String CACHE_2_15s = "cache_2_15s";
  public static final String CACHE_3_15s = "cache_3_15s";
  public static final String CACHE_4_15s = "cache_4_15s";
  public static final String CACHE_5_15s = "cache_5_15s";
  public static final String CACHE_6_15s = "cache_6_15s";
  public static final String CACHE_7_15s = "cache_7_15s";
  public static final String CACHE_8_15s = "cache_8_15s";
  public static final String CACHE_9_15s = "cache_9_15s";
  public static final String CACHE_10_15s = "cache_10_15s";

  // ===== 20s =====
  public static final String CACHE_20s = "cache_20s";
  public static final String CACHE_2_20s = "cache_2_20s";
  public static final String CACHE_3_20s = "cache_3_20s";
  public static final String CACHE_4_20s = "cache_4_20s";
  public static final String CACHE_5_20s = "cache_5_20s";
  public static final String CACHE_6_20s = "cache_6_20s";
  public static final String CACHE_7_20s = "cache_7_20s";
  public static final String CACHE_8_20s = "cache_8_20s";
  public static final String CACHE_9_20s = "cache_9_20s";
  public static final String CACHE_10_20s = "cache_10_20s";

  // ===== 30s =====
  public static final String CACHE_30s = "cache_30s";
  public static final String CACHE_2_30s = "cache_2_30s";
  public static final String CACHE_3_30s = "cache_3_30s";
  public static final String CACHE_4_30s = "cache_4_30s";
  public static final String CACHE_5_30s = "cache_5_30s";
  public static final String CACHE_6_30s = "cache_6_30s";
  public static final String CACHE_7_30s = "cache_7_30s";
  public static final String CACHE_8_30s = "cache_8_30s";
  public static final String CACHE_9_30s = "cache_9_30s";
  public static final String CACHE_10_30s = "cache_10_30s";

  // ===== 45s =====
  public static final String CACHE_45s = "cache_45s";
  public static final String CACHE_2_45s = "cache_2_45s";
  public static final String CACHE_3_45s = "cache_3_45s";
  public static final String CACHE_4_45s = "cache_4_45s";
  public static final String CACHE_5_45s = "cache_5_45s";
  public static final String CACHE_6_45s = "cache_6_45s";
  public static final String CACHE_7_45s = "cache_7_45s";
  public static final String CACHE_8_45s = "cache_8_45s";
  public static final String CACHE_9_45s = "cache_9_45s";
  public static final String CACHE_10_45s = "cache_10_45s";


  // ===== 1m =====
  public static final String CACHE_1m = "cache_1m";
  public static final String CACHE_2_1m = "cache_2_1m";
  public static final String CACHE_3_1m = "cache_3_1m";
  public static final String CACHE_4_1m = "cache_4_1m";
  public static final String CACHE_5_1m = "cache_5_1m";
  public static final String CACHE_6_1m = "cache_6_1m";
  public static final String CACHE_7_1m = "cache_7_1m";
  public static final String CACHE_8_1m = "cache_8_1m";
  public static final String CACHE_9_1m = "cache_9_1m";
  public static final String CACHE_10_1m = "cache_10_1m";

  // ===== 2m =====
  public static final String CACHE_2m = "cache_2m";
  public static final String CACHE_2_2m = "cache_2_2m";
  public static final String CACHE_3_2m = "cache_3_2m";
  public static final String CACHE_4_2m = "cache_4_2m";
  public static final String CACHE_5_2m = "cache_5_2m";
  public static final String CACHE_6_2m = "cache_6_2m";
  public static final String CACHE_7_2m = "cache_7_2m";
  public static final String CACHE_8_2m = "cache_8_2m";
  public static final String CACHE_9_2m = "cache_9_2m";
  public static final String CACHE_10_2m = "cache_10_2m";

  // ===== 3m =====
  public static final String CACHE_3m = "cache_3m";
  public static final String CACHE_2_3m = "cache_2_3m";
  public static final String CACHE_3_3m = "cache_3_3m";
  public static final String CACHE_4_3m = "cache_4_3m";
  public static final String CACHE_5_3m = "cache_5_3m";
  public static final String CACHE_6_3m = "cache_6_3m";
  public static final String CACHE_7_3m = "cache_7_3m";
  public static final String CACHE_8_3m = "cache_8_3m";
  public static final String CACHE_9_3m = "cache_9_3m";
  public static final String CACHE_10_3m = "cache_10_3m";

  // ===== 5m =====
  public static final String CACHE_5m = "cache_5m";
  public static final String CACHE_2_5m = "cache_2_5m";
  public static final String CACHE_3_5m = "cache_3_5m";
  public static final String CACHE_4_5m = "cache_4_5m";
  public static final String CACHE_5_5m = "cache_5_5m";
  public static final String CACHE_6_5m = "cache_6_5m";
  public static final String CACHE_7_5m = "cache_7_5m";
  public static final String CACHE_8_5m = "cache_8_5m";
  public static final String CACHE_9_5m = "cache_9_5m";
  public static final String CACHE_10_5m = "cache_10_5m";

  // ===== 10m =====
  public static final String CACHE_10m = "cache_10m";
  public static final String CACHE_2_10m = "cache_2_10m";
  public static final String CACHE_3_10m = "cache_3_10m";
  public static final String CACHE_4_10m = "cache_4_10m";
  public static final String CACHE_5_10m = "cache_5_10m";
  public static final String CACHE_6_10m = "cache_6_10m";
  public static final String CACHE_7_10m = "cache_7_10m";
  public static final String CACHE_8_10m = "cache_8_10m";
  public static final String CACHE_9_10m = "cache_9_10m";
  public static final String CACHE_10_10m = "cache_10_10m";

  // ===== 15m =====
  public static final String CACHE_15m = "cache_15m";
  public static final String CACHE_2_15m = "cache_2_15m";
  public static final String CACHE_3_15m = "cache_3_15m";
  public static final String CACHE_4_15m = "cache_4_15m";
  public static final String CACHE_5_15m = "cache_5_15m";
  public static final String CACHE_6_15m = "cache_6_15m";
  public static final String CACHE_7_15m = "cache_7_15m";
  public static final String CACHE_8_15m = "cache_8_15m";
  public static final String CACHE_9_15m = "cache_9_15m";
  public static final String CACHE_10_15m = "cache_10_15m";

  // ===== 20m =====
  public static final String CACHE_20m = "cache_20m";
  public static final String CACHE_2_20m = "cache_2_20m";
  public static final String CACHE_3_20m = "cache_3_20m";
  public static final String CACHE_4_20m = "cache_4_20m";
  public static final String CACHE_5_20m = "cache_5_20m";
  public static final String CACHE_6_20m = "cache_6_20m";
  public static final String CACHE_7_20m = "cache_7_20m";
  public static final String CACHE_8_20m = "cache_8_20m";
  public static final String CACHE_9_20m = "cache_9_20m";
  public static final String CACHE_10_20m = "cache_10_20m";

  // ===== 30m =====
  public static final String CACHE_30m = "cache_30m";
  public static final String CACHE_2_30m = "cache_2_30m";
  public static final String CACHE_3_30m = "cache_3_30m";
  public static final String CACHE_4_30m = "cache_4_30m";
  public static final String CACHE_5_30m = "cache_5_30m";
  public static final String CACHE_6_30m = "cache_6_30m";
  public static final String CACHE_7_30m = "cache_7_30m";
  public static final String CACHE_8_30m = "cache_8_30m";
  public static final String CACHE_9_30m = "cache_9_30m";
  public static final String CACHE_10_30m = "cache_10_30m";


  // ===== 1h =====
  public static final String CACHE_1h = "cache_1h";
  public static final String CACHE_2_1h = "cache_2_1h";
  public static final String CACHE_3_1h = "cache_3_1h";
  public static final String CACHE_4_1h = "cache_4_1h";
  public static final String CACHE_5_1h = "cache_5_1h";
  public static final String CACHE_6_1h = "cache_6_1h";
  public static final String CACHE_7_1h = "cache_7_1h";
  public static final String CACHE_8_1h = "cache_8_1h";
  public static final String CACHE_9_1h = "cache_9_1h";
  public static final String CACHE_10_1h = "cache_10_1h";

  // ===== 2h =====
  public static final String CACHE_2h = "cache_2h";
  public static final String CACHE_2_2h = "cache_2_2h";
  public static final String CACHE_3_2h = "cache_3_2h";
  public static final String CACHE_4_2h = "cache_4_2h";
  public static final String CACHE_5_2h = "cache_5_2h";
  public static final String CACHE_6_2h = "cache_6_2h";
  public static final String CACHE_7_2h = "cache_7_2h";
  public static final String CACHE_8_2h = "cache_8_2h";
  public static final String CACHE_9_2h = "cache_9_2h";
  public static final String CACHE_10_2h = "cache_10_2h";

  // ===== 3h =====
  public static final String CACHE_3h = "cache_3h";
  public static final String CACHE_2_3h = "cache_2_3h";
  public static final String CACHE_3_3h = "cache_3_3h";
  public static final String CACHE_4_3h = "cache_4_3h";
  public static final String CACHE_5_3h = "cache_5_3h";
  public static final String CACHE_6_3h = "cache_6_3h";
  public static final String CACHE_7_3h = "cache_7_3h";
  public static final String CACHE_8_3h = "cache_8_3h";
  public static final String CACHE_9_3h = "cache_9_3h";
  public static final String CACHE_10_3h = "cache_10_3h";

  // ===== 5h =====
  public static final String CACHE_5h = "cache_5h";
  public static final String CACHE_2_5h = "cache_2_5h";
  public static final String CACHE_3_5h = "cache_3_5h";
  public static final String CACHE_4_5h = "cache_4_5h";
  public static final String CACHE_5_5h = "cache_5_5h";
  public static final String CACHE_6_5h = "cache_6_5h";
  public static final String CACHE_7_5h = "cache_7_5h";
  public static final String CACHE_8_5h = "cache_8_5h";
  public static final String CACHE_9_5h = "cache_9_5h";
  public static final String CACHE_10_5h = "cache_10_5h";

  // ===== 10h =====
  public static final String CACHE_10h = "cache_10h";
  public static final String CACHE_2_10h = "cache_2_10h";
  public static final String CACHE_3_10h = "cache_3_10h";
  public static final String CACHE_4_10h = "cache_4_10h";
  public static final String CACHE_5_10h = "cache_5_10h";
  public static final String CACHE_6_10h = "cache_6_10h";
  public static final String CACHE_7_10h = "cache_7_10h";
  public static final String CACHE_8_10h = "cache_8_10h";
  public static final String CACHE_9_10h = "cache_9_10h";
  public static final String CACHE_10_10h = "cache_10_10h";

  // ===== 15h =====
  public static final String CACHE_15h = "cache_15h";
  public static final String CACHE_2_15h = "cache_2_15h";
  public static final String CACHE_3_15h = "cache_3_15h";
  public static final String CACHE_4_15h = "cache_4_15h";
  public static final String CACHE_5_15h = "cache_5_15h";
  public static final String CACHE_6_15h = "cache_6_15h";
  public static final String CACHE_7_15h = "cache_7_15h";
  public static final String CACHE_8_15h = "cache_8_15h";
  public static final String CACHE_9_15h = "cache_9_15h";
  public static final String CACHE_10_15h = "cache_10_15h";


  // ===== 1d =====
  public static final String CACHE_1d = "cache_1d";
  public static final String CACHE_2_1d = "cache_2_1d";
  public static final String CACHE_3_1d = "cache_3_1d";
  public static final String CACHE_4_1d = "cache_4_1d";
  public static final String CACHE_5_1d = "cache_5_1d";
  public static final String CACHE_6_1d = "cache_6_1d";
  public static final String CACHE_7_1d = "cache_7_1d";
  public static final String CACHE_8_1d = "cache_8_1d";
  public static final String CACHE_9_1d = "cache_9_1d";
  public static final String CACHE_10_1d = "cache_10_1d";

  // ===== 2d =====
  public static final String CACHE_2d = "cache_2d";
  public static final String CACHE_2_2d = "cache_2_2d";
  public static final String CACHE_3_2d = "cache_3_2d";
  public static final String CACHE_4_2d = "cache_4_2d";
  public static final String CACHE_5_2d = "cache_5_2d";
  public static final String CACHE_6_2d = "cache_6_2d";
  public static final String CACHE_7_2d = "cache_7_2d";
  public static final String CACHE_8_2d = "cache_8_2d";
  public static final String CACHE_9_2d = "cache_9_2d";
  public static final String CACHE_10_2d = "cache_10_2d";

  // ===== 3d =====
  public static final String CACHE_3d = "cache_3d";
  public static final String CACHE_2_3d = "cache_2_3d";
  public static final String CACHE_3_3d = "cache_3_3d";
  public static final String CACHE_4_3d = "cache_4_3d";
  public static final String CACHE_5_3d = "cache_5_3d";
  public static final String CACHE_6_3d = "cache_6_3d";
  public static final String CACHE_7_3d = "cache_7_3d";
  public static final String CACHE_8_3d = "cache_8_3d";
  public static final String CACHE_9_3d = "cache_9_3d";
  public static final String CACHE_10_3d = "cache_10_3d";

  // ===== 5d =====
  public static final String CACHE_5d = "cache_5d";
  public static final String CACHE_2_5d = "cache_2_5d";
  public static final String CACHE_3_5d = "cache_3_5d";
  public static final String CACHE_4_5d = "cache_4_5d";
  public static final String CACHE_5_5d = "cache_5_5d";
  public static final String CACHE_6_5d = "cache_6_5d";
  public static final String CACHE_7_5d = "cache_7_5d";
  public static final String CACHE_8_5d = "cache_8_5d";
  public static final String CACHE_9_5d = "cache_9_5d";
  public static final String CACHE_10_5d = "cache_10_5d";

  // ===== 10d =====
  public static final String CACHE_10d = "cache_10d";
  public static final String CACHE_2_10d = "cache_2_10d";
  public static final String CACHE_3_10d = "cache_3_10d";
  public static final String CACHE_4_10d = "cache_4_10d";
  public static final String CACHE_5_10d = "cache_5_10d";
  public static final String CACHE_6_10d = "cache_6_10d";
  public static final String CACHE_7_10d = "cache_7_10d";
  public static final String CACHE_8_10d = "cache_8_10d";
  public static final String CACHE_9_10d = "cache_9_10d";
  public static final String CACHE_10_10d = "cache_10_10d";

  // ===== 15d =====
  public static final String CACHE_15d = "cache_15d";
  public static final String CACHE_2_15d = "cache_2_15d";
  public static final String CACHE_3_15d = "cache_3_15d";
  public static final String CACHE_4_15d = "cache_4_15d";
  public static final String CACHE_5_15d = "cache_5_15d";
  public static final String CACHE_6_15d = "cache_6_15d";
  public static final String CACHE_7_15d = "cache_7_15d";
  public static final String CACHE_8_15d = "cache_8_15d";
  public static final String CACHE_9_15d = "cache_9_15d";
  public static final String CACHE_10_15d = "cache_10_15d";

  // ===== 30d =====
  public static final String CACHE_30d = "cache_30d";
  public static final String CACHE_2_30d = "cache_2_30d";
  public static final String CACHE_3_30d = "cache_3_30d";
  public static final String CACHE_4_30d = "cache_4_30d";
  public static final String CACHE_5_30d = "cache_5_30d";
  public static final String CACHE_6_30d = "cache_6_30d";
  public static final String CACHE_7_30d = "cache_7_30d";
  public static final String CACHE_8_30d = "cache_8_30d";
  public static final String CACHE_9_30d = "cache_9_30d";
  public static final String CACHE_10_30d = "cache_10_30d";


  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    List<Cache> cacheLists = Lists.newArrayList();
    String el = "maximumSize=50000, expireAfterWrite=";
    Field[] fields = LocalCaffeineCache.class.getFields();
    for (Field field : fields) {
      String cacheName = field.getName();
      String[] strings = cacheName.split("_");
      logger.info("LocalCache:cacheName:{}:el:{}", cacheName, el + strings[strings.length - 1]);
      cacheLists.add(buildCaffeineCache(cacheName, el + strings[strings.length - 1]));
    }
    cacheManager.setCaches(cacheLists);
    return cacheManager;
  }

  public Cache buildCaffeineCache(String key, String cacheEl) {
    return new CaffeineCache(key, Caffeine.from(cacheEl).build());
  }

}
