package abistech.resseract.util.cache;

import abistech.resseract.exception.ResseractException;

public interface CacheHandler<K, V> {

    V handle(K key) throws ResseractException;
}
