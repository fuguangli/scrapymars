package com.automata.schedule.mode;

import com.alibaba.fastjson.JSONObject;
import com.automata.request.Request;
import com.automata.schedule.Scheduler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
