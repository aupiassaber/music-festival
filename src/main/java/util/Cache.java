package util;

import com.github.benmanes.caffeine.cache.Caffeine;

import javax.json.JsonArray;
import java.util.concurrent.TimeUnit;

/**
 * This object uses the Caffeine in memory caching library to store/get JsonArray objects
 *
 * @author Aupi As-Saber
 */
public class Cache {
    private static final Cache CACHE_INSTANCE = new Cache();
    private com.github.benmanes.caffeine.cache.Cache<String, JsonArray> jsonArrayCache;

    private Cache() {
        jsonArrayCache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(10)
                .build();
    }

    public JsonArray getJsonArrayCache(String key) {
        return jsonArrayCache.getIfPresent(key);
    }

    public void putJsonArrayCache(String key, JsonArray jsonArray) {
        this.jsonArrayCache.put(key, jsonArray);
    }

    public static Cache getCacheInstance() {
        return CACHE_INSTANCE;
    }
}
