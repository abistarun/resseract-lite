package abistech.resseract.async.threadpool;

import abistech.resseract.util.Constants;
import abistech.resseract.util.Util;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    private static ThreadPool instance;
    private final ExecutorService pool;

    private ThreadPool(int threadPoolSize) {
        pool = Executors.newFixedThreadPool(threadPoolSize);
    }

    public static void initialize(Properties properties) {
        String threadPoolSize = properties.getProperty(Constants.THREAD_POOL_SIZE);
        if (Util.isValidString(threadPoolSize))
            instance = new ThreadPool(Integer.parseInt(threadPoolSize));
    }

    public static ThreadPool getInstance() {
        return instance;
    }

    public void submitTask(Runnable runnable) {
        WrappedRunnable wrappedRunnable = new WrappedRunnable(runnable);
        pool.submit(wrappedRunnable);
    }

    public static void close() {
        if (getInstance() != null)
            getInstance().pool.shutdown();
    }
}

