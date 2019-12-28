package com.automata.downwares;

import com.automata.downloader.IDownloader;
import com.automata.request.Request;

import java.util.Map;

/**
 * name fuguangli
 * date 2019/12/24
 * contact businessfgl@163.com
 */
public interface IDownloadWare {
    IDownloader option(Map<String,Object> args);

    void wrapRequest(Request request);
}
