package abistech.resseract.util.cache;

import abistech.resseract.exception.ResseractException;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ResseractCacheTest {

    @Test
    public void testCacheForResult() throws ResseractException {
        ResseractCache<String, Integer> cache = new ResseractCache<>(30);

        AtomicInteger atomicInteger = new AtomicInteger(1);

        for (int i = 0; i < 3; i++)
            new Thread(() -> {
                try {
                    cache.handle("Key1", key -> atomicInteger.incrementAndGet());
                } catch (ResseractException e) {
                    e.printStackTrace();
                    fail();
                }
            }).start();

        int cachedValue = cache.handle("Key1", key -> atomicInteger.incrementAndGet());
        Assert.assertEquals(2, cachedValue);
        Assert.assertEquals(2, atomicInteger.get());

        cachedValue = cache.handle("Key2", key -> atomicInteger.incrementAndGet());
        Assert.assertEquals(3, cachedValue);
        Assert.assertEquals(3, atomicInteger.get());
    }

}