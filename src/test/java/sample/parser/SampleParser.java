package sample.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.automata.parser.IResponseParser;
import com.automata.queues.StorageQueue;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;

/**
 * name fuguangli
 * date 2019/12/24
 * contact businessfgl@163.com
 * <p>
 * 示例解析器
 */
public class SampleParser implements IResponseParser {
    Logger logger = LoggerFactory.getLogger(SampleParser.class);

    public Object parse(InputStream stream) {
        return null;
    }

    @Autowired
    @Qualifier("internalUrlQueue")
    StorageQueue internalUrlQueue;

    public Object parse(String response) {
        /*{"url":"xxx","content":"xxx"}*/
        logger.debug("parsing response");
        JSONObject res = JSON.parseObject(response);
        String content = res.getString("content");
        String url = res.getString("url");
        logger.debug("parse URL-->" + url);
        Document doc = Jsoup.parse(content);
        String builtForDevelopers = doc.select("title").text();
        logger.info("get result-->" + builtForDevelopers);

        String href = doc.select("a.jumbotron-link").eq(0).attr("href");
        if (StringUtils.isNotBlank(href)) {
            internalUrlQueue.push("https://github.com" + href);
            logger.info("push to internalUrlQueue");
        }
        return null;
    }
}
