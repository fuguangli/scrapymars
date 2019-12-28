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
public class OnceScheduler extends Scheduler {
    Logger logger = LoggerFactory.getLogger(Scheduler.class);

    public void scheduled() {
        if (CollectionUtils.isNotEmpty(getStartUrls())) {
            for (String url : getStartUrls()) {
                logger.debug("request url-->" + url);
                String content = getDownloader().getContent(new Request(url));
                if (StringUtils.isNotBlank(content)) {
                    JSONObject block = new JSONObject();
                    block.put("url", url);
                    block.put("content", content);

                    getResponseParser().parse(block.toJSONString());
                }
            }
        }
    }
}
