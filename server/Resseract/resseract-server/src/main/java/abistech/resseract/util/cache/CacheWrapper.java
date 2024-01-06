package abistech.resseract.util.cache;

import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;

import java.util.concurrent.CountDownLatch;

class CacheWrapper<V> {

    private final CountDownLatch latch;
    private V result;
    private ResseractException exception;

    CacheWrapper() {
        latch = new CountDownLatch(1);
    }

    void unlock() {
        latch.countDown();
    }

    void await() {
        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }
    }

    public void setResult(V result) {
        this.result = result;
    }

    public void setException(Exception exception) {
        if (exception instanceof ResseractException)
            this.exception = (ResseractException) exception;
        else
            this.exception = new ResseractException(CustomErrorReports.UNKNOWN_ERROR, exception);
    }

    public V getResult() throws ResseractException {
        if (exception != null)
            throw exception;
        return result;
    }
}
