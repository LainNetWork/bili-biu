package fun.lain.bilibiu.cache.tunnel;


import java.util.concurrent.*;

/**
 *
 */
public class CacheTunnel {
    //线程池
    private static final ThreadPoolExecutor exec = new ThreadPoolExecutor(5,5,0L, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(25));
    //回调队列
    public static final LinkedBlockingQueue<Future<CachePartResult>> futureQueue = new LinkedBlockingQueue();

    public static Future<CachePartResult> submit(Callable<CachePartResult> callable){
        return exec.submit(callable);
    }
    public static Future submit(Runnable runnable){
        return exec.submit(runnable);
    }
}
