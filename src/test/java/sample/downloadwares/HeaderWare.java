package sample.downloadwares;

import com.automata.downloader.IDownloader;
import com.automata.downwares.IDownloadWare;
import com.automata.request.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * name fuguangli
 * date 2019/12/28
 * contact businessfgl@163.com
 */
public class HeaderWare implements IDownloadWare {
    @Override
    public IDownloader option(Map<String, Object> args) {
        return null;
    }

    @Override
    public void wrapRequest(Request request) {
        if (request.getHeaders() == null) {
            request.setHeaders(new HashMap<>());
            request.getHeaders().put("Accept-Language", "zh-CN,zh;q=0.9");
        } else {
            request.getHeaders().put("Accept-Language", "zh-CN,zh;q=0.9");
        }
    }
}
