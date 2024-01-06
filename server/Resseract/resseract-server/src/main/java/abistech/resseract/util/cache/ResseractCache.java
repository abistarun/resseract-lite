package abistech.resseract.util.cache;

import abistech.resseract.exception.ResseractException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class ResseractCache<K, V> {

    private final Cache<K, CacheWrapper<V>> cache;

    public ResseractCache(int cacheInterval) {
        cache = CacheBuilder.newBuilder().expireAfterAccess(cacheInterval, TimeUnit.SECONDS).build();
    }

    public V handle(K key, CacheHandler<K, V> handler) throws ResseractException {
        CacheWrapper<V> value;
        CacheWrapper<V> cacheWrapper = null;
        synchronized (this) {
            value = cache.getIfPresent(key);
            if (value == null) {
                cacheWrapper = new CacheWrapper<>();
                cache.put(key, cacheWrapper);
            }
        }
        if (value == null) {
            try {
                V result = handler.handle(key);
                cacheWrapper.setResult(result);
            } catch (Exception e) {
                cacheWrapper.setException(e);
            }
            cacheWrapper.unlock();
            return cacheWrapper.getResult();
        } else {
            value.await();
            return value.getResult();
        }
    }

    public void remove(K key) {
        cache.invalidate(key);
    }
}
