package com.automata.threads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * name fuguangli
 * date 2019/12/25
 * contact businessfgl@163.com
 */
public class ThreadPools {
    private ExecutorService downloadExecutors;
    private ExecutorService parseExecutors;

    public ThreadPools() {
        downloadExecutors = new ThreadPoolExecutor(
                5, 20,
                5, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100),
                new ThreadPoolExecutor.AbortPolicy()
        );
        parseExecutors = new ThreadPoolExecutor(
                5, 20,
                5, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    public ThreadPools(int corePoolSize, int maximumPoolSize, int keepAliveSeconds) {
        downloadExecutors = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                keepAliveSeconds, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100),
                new ThreadPoolExecutor.AbortPolicy()
        );
        parseExecutors = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                keepAliveSeconds, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    public void addDownloadTask(Runnable task) {
        downloadExecutors.execute(task);
    }

    public void addParseTask(Runnable task) {
        parseExecutors.execute(task);
    }
}
