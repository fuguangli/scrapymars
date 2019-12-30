package com.automata.queues.internal;

import com.automata.queues.StorageQueue;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * name fuguangli
 * date 2019/12/24
 * contact businessfgl@163.com
 */
public class InternalResponseQueue implements StorageQueue {
    private LinkedBlockingQueue<String> queue;

    public InternalResponseQueue() {
        queue = new LinkedBlockingQueue<>(10);
    }

    public InternalResponseQueue(Integer capacity) {
        queue = new LinkedBlockingQueue<>(capacity);
    }

    public Object push(Object args) {
        return queue.offer((String) args);
    }

    public Object pull(Object args) {
        return queue.poll();
    }

    @Override
    public Object blockPoll(Long waitSeconds) {
        try {
            return queue.poll(waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object blockPush(Long waitSeconds, Object element) {

        try {
            return queue.offer((String) element, waitSeconds, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
