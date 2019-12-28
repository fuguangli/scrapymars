package com.automata.downloader;

import com.alibaba.fastjson.JSONObject;
import com.automata.request.Request;

import java.io.InputStream;

/**
 * name fuguangli
 * date 2019/12/24
 * contact businessfgl@163.com
 */
public interface IDownloader {
    String getContent(Request req);

    JSONObject getJsonContent(Request req);

    InputStream getBContent(Request req);

    void closeClient();

    Object getClient();

    void initClient();
}
