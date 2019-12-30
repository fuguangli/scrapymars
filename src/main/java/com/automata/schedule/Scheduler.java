package com.automata.schedule;

import com.alibaba.fastjson.JSONObject;
import com.automata.downloader.IDownloader;
import com.automata.parser.IResponseParser;
import com.automata.queues.StorageQueue;
import com.automata.request.Request;
import com.automata.threads.ThreadPools;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
/**
 * name fuguangli
 * date 2019/12/25
 * contact businessfgl@163.com
 */
public abstract class Scheduler {
    private ThreadPools threadPools;
    private StorageQueue urlQueue;
    private StorageQueue responseQueue;

    private IDownloader downloader;
    private IResponseParser responseParser;

    private List<String> startUrls;

    /*for urlQueue*/
    protected boolean isRunning = true;
    /*for responseQueue*/
    protected boolean isRunningR = true;

    private Integer pollWaitSeconds = 10;

    public Scheduler() {

    }

    /**
     * core method
     */
    public abstract void scheduled();

    protected void startParseThread(boolean isManual) {
        getThreadPools().addParseTask(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!isManual && !isRunning && !isRunningR) {
                        break;
                    }
                    try {
                        final String response = (String) getResponseQueue().blockPull(getPollWaitSeconds().longValue());
                        if (StringUtils.isNotBlank(response)) {
                            isRunningR = true;
                            getThreadPools().addParseTask(new Runnable() {
                                public void run() {
                                    getResponseParser().parse(response);
                                }
                            });
                        } else {
                            isRunningR = false;
                        }

                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void startUrlQueueThread(boolean isManual) {
        getThreadPools().addDownloadTask(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!isManual && !isRunning && !isRunningR) {
                        break;
                    }
                    try {
                        final String url = (String) getUrlQueue().blockPull(getPollWaitSeconds().longValue());
                        if (StringUtils.isNotBlank(url)) {
                            isRunning = true;
                            getThreadPools().addDownloadTask(new Runnable() {
                                public void run() {
                                    String content = getDownloader().getContent(new Request(url));
                                    if (StringUtils.isNotBlank(content)) {
                                        JSONObject block = new JSONObject();
                                        block.put("url", url);
                                        block.put("content", content);
                                        getResponseQueue().push(block.toJSONString());
                                    }
                                }
                            });
                        } else {
                            isRunning = false;
                        }

                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public ThreadPools getThreadPools() {
        return threadPools;
    }

    public void setThreadPools(ThreadPools threadPools) {
        this.threadPools = threadPools;
    }

    public StorageQueue getUrlQueue() {
        return urlQueue;
    }

    public void setUrlQueue(StorageQueue urlQueue) {
        this.urlQueue = urlQueue;
    }

    public StorageQueue getResponseQueue() {
        return responseQueue;
    }

    public void setResponseQueue(StorageQueue responseQueue) {
        this.responseQueue = responseQueue;
    }

    public IResponseParser getResponseParser() {
        return responseParser;
    }

    public void setResponseParser(IResponseParser responseParser) {
        this.responseParser = responseParser;
    }

    public List<String> getStartUrls() {
        return startUrls;
    }

    public void setStartUrls(List<String> startUrls) {
        this.startUrls = startUrls;
    }

    public Integer getPollWaitSeconds() {
        return pollWaitSeconds;
    }

    public void setPollWaitSeconds(Integer pollWaitSeconds) {
        this.pollWaitSeconds = pollWaitSeconds;
    }

    public IDownloader getDownloader() {
        return downloader;
    }

    public void setDownloader(IDownloader downloader) {
        this.downloader = downloader;
    }
}
