package com.automata.queues.internal;

import com.automata.queues.StorageQueue;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * name fuguangli
 * date 2019/12/24
 * contact businessfgl@163.com
 */
public class InternalUrlQueue implements StorageQueue {
    private LinkedBlockingQueue<String> queue;

    public InternalUrlQueue() {
        queue = new LinkedBlockingQueue<>(10);
    }

    public InternalUrlQueue(Integer capacity) {
        queue = new LinkedBlockingQueue<>(capacity);
    }

    public Object push(Object args) {
        return queue.offer((String) args);
    }

    public Object pull(Object args) {
        return queue.poll();
    }

    public Object getQueue() {
        return this.queue;
    }
}
