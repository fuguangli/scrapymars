package com.automata.queues;


/**
 * name fuguangli
 * date 2019/12/24
 * contact businessfgl@163.com
 */
public interface StorageQueue {
    /**
     * push to queue
     *
     * @param args
     * @return
     */
    Object push(Object args);

    /**
     * pull from queue
     *
     * @param args
     * @return
     */
    Object pull(Object args);

    Object blockPull(Long waitSeconds);

    Object blockPush(Long waitSeconds,Object element);
}
