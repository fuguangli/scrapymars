package com.automata.engine;

import com.automata.downloader.Downloader;
import com.automata.schedule.Scheduler;
import com.automata.schedule.mode.OnceScheduler;

import java.util.List;

/**
 * name fuguangli
 * date 2019/12/25
 * contact businessfgl@163.com
 */
public class ScrapyEngine {
    private Scheduler scheduler;

    private List<String> startUrls;

    public ScrapyEngine() {
    }

    public void defaultInstances() {
        Downloader downloader = new Downloader();
        downloader.initClient();
        scheduler = new OnceScheduler();

        scheduler.setDownloader(downloader);
        scheduler.setStartUrls(startUrls);
    }

    public void start() {
        scheduler.scheduled();
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public List<String> getStartUrls() {
        return startUrls;
    }

    public void setStartUrls(List<String> startUrls) {
        this.startUrls = startUrls;
    }
}
