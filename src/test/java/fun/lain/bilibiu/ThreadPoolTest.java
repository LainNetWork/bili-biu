package fun.lain.bilibiu;


import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class ThreadPoolTest {
    private static final ArrayBlockingQueue queue = new ArrayBlockingQueue(10);
    private static final ExecutorService exec = new ThreadPoolExecutor(5,5,0L,TimeUnit.MILLISECONDS,queue);
    public static void main(String[] args) {
//        for(int i = 0;i<100;i++){
//            Future fi = exec.submit(()->{
//                System.out.println(Thread.currentThread().getName() + "---------------------执行中!");
//                try {
//                    System.out.println(queue.size());
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(Thread.currentThread().getName() + "---------------------执行完毕!");
//            });
//            System.out.println(queue.size());
//
//
//        }
//        System.out.println(exec.isTerminated());
        ArrayBlockingQueue<Runnable> queue2 = new ArrayBlockingQueue(10);
        for(int i=0;i<10;i++){
            try {
                final int i2 = i;
                queue2.put(()->{
                    System.out.println(Thread.currentThread().getName()+ " count:"+i2);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ExecutorService executorService = new ThreadPoolExecutor(2,2,1,TimeUnit.SECONDS,queue2);

    }
}
