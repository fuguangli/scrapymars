package com.automata.schedule.mode;

import com.automata.schedule.Scheduler;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * name fuguangli
 * date 2019/12/25
 * contact businessfgl@163.com
 */
public class AlwaysScheduler extends Scheduler {
    Logger logger = LoggerFactory.getLogger(AlwaysScheduler.class);
    private Integer repeatIntervalSeconds;

    public void scheduled() {
        if (CollectionUtils.isNotEmpty(getStartUrls())) {
            startParseThread(true);
            startUrlQueueThread(true);

            while (true) {
                try {
                    for (final String url : getStartUrls()) {
                        logger.debug("request url-->" + url);
                        addDownload(url);
                    }

                    Thread.sleep(getRepeatIntervalSeconds() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public Integer getRepeatIntervalSeconds() {
        return repeatIntervalSeconds;
    }

    public void setRepeatIntervalSeconds(Integer repeatIntervalSeconds) {
        this.repeatIntervalSeconds = repeatIntervalSeconds;
    }
}
