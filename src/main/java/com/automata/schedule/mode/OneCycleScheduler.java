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
public class OneCycleScheduler extends Scheduler {

    Logger logger = LoggerFactory.getLogger(OneCycleScheduler.class);

    public void scheduled() {
        if (CollectionUtils.isNotEmpty(getStartUrls())) {
            startParseThread(false);
//            startUrlQueueThread(false);

            for (final String url : getStartUrls()) {
                logger.debug("request url-->" + url);
                getThreadPools().addDownloadTask(new Runnable() {
                    public void run() {
                        String content = getDownloader().getContent(new Request(url));
                        if (StringUtils.isNotBlank(content)) {
                            JSONObject block = new JSONObject();
                            block.put("url", url);
                            block.put("content", content);
                            logger.debug("push response");
                            getResponseQueue().push(block.toJSONString());
                        }
                    }
                });

            }

            while (true) {
                if (!isRunning && !isRunningR) {
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
    }

}
