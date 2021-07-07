package com.example.halo.demo.test;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description:  在并发环境下，有一个自增器，每调一次加一，超过1000，重置为0，再进行自增，如何实现？
 * @Author: Halo_ry
 * @Date: 2020/3/28 15:39
 */
@Slf4j
public class Autoincrement {
    private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("autoincrement-pool-%d").build();
    private static int core = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executorService = new ThreadPoolExecutor(core * 2, core * 2,
            0L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(), threadFactory, new ThreadPoolExecutor.AbortPolicy());

    private static long count = 0;
    private static AtomicLong atomicLong = new AtomicLong(0);

    @Test
    public void test() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 10000; i++) {
            getNum();
        }
    }

    private Long getNum() throws ExecutionException, InterruptedException {
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
//            Callable callable = this::increment;
            Callable callable = this::increment2;
            futures.add(executorService.submit(callable));
        }

        for (Future<Long> future : futures) {
            long num = future.get();
            System.out.println("num数：" + num);
        }
        return null;
    }

    /**
     * 加锁
     * @return
     */
    private synchronized long increment() {
        if (count > 1000) {
            count = 0;
        }
        return count++;
    }

    /**
     * 取模
     * @return
     */
    private long increment2() {
        return atomicLong.getAndIncrement() % 1001;
    }
}
