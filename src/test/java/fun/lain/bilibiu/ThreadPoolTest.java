package fun.lain.bilibiu;


import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class ThreadPoolTest {
    private static final ArrayBlockingQueue queue = new ArrayBlockingQueue(10);
    private static final ExecutorService exec = new ThreadPoolExecutor(5,5,0L,TimeUnit.MILLISECONDS,queue);
    public static void main(String[] args) {
        for(int i = 0;i<100;i++){
            Future fi = exec.submit(()->{
                System.out.println(Thread.currentThread().getName() + "---------------------执行中!");
                try {
                    System.out.println(queue.size());
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "---------------------执行完毕!");
            });
            System.out.println(queue.size());


        }
        System.out.println(exec.isTerminated());
    }
}
