package com.automata.downloader;

import com.alibaba.fastjson.JSONObject;
import com.automata.config.DownloaderEnum;
import com.automata.downwares.IDownloadWare;
import com.automata.downwares.ProxyWare;
import com.automata.request.Request;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

/**
 * name fuguangli
 * date 2019/12/24
 * contact businessfgl@163.com
 */
public class Downloader implements IDownloader {
    Logger logger = LoggerFactory.getLogger(Downloader.class);

    private WebClient webClient = null;
    private CloseableHttpClient httpClient = null;

    private Integer downloaderSelect = DownloaderEnum.FOR_XHR.getValue();

    private ProxyWare proxyWare;
    private Integer timeout = 10000;

    private List<IDownloadWare> downloadWares;

    public Downloader() {
    }

    public Downloader(WebClient webClient) {
        this.webClient = webClient;
    }

    private void wrap(Request req) {
        if (CollectionUtils.isNotEmpty(downloadWares)) {
            for (IDownloadWare ware : downloadWares) {
                ware.wrapRequest(req);
            }
        }
    }

    private void wrapWebClient() {
        this.webClient = new WebClient(BrowserVersion.CHROME);

        webClient.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
        webClient.addRequestHeader("Cache-Control", "max-age=0");

        webClient.getOptions().setTimeout(timeout);
        webClient.getOptions().setJavaScriptEnabled(true);//启用js  仅仅能够获取到html中直接执行的js，对于ajax的js程序，无法获取到值
        webClient.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getCookieManager().setCookiesEnabled(true);

        webClient.waitForBackgroundJavaScript(timeout);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        if (this.proxyWare != null) {
            //设置代理
            ProxyConfig proxyConfig = getWebClient().getOptions().getProxyConfig();
            proxyConfig.setProxyHost(proxyWare.getProxyHost());
            proxyConfig.setProxyPort(proxyWare.getProxyPort());
            if (this.proxyWare.getProxyUser() != null) {
                DefaultCredentialsProvider credentialsProvider = (DefaultCredentialsProvider) getWebClient().getCredentialsProvider();
                credentialsProvider.addCredentials(proxyWare.getProxyUser(), proxyWare.getProxyPass());
            }
        }
    }

    private void wrapHttpclient() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] arg0,
                                           String arg1) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] arg0,
                                           String arg1) throws CertificateException {
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                ctx, NoopHostnameVerifier.INSTANCE);
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslConnectionSocketFactory)
                .register("http", plainsf)
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
        cm.setMaxTotal(20);
        cm.setDefaultMaxPerRoute(15);

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoKeepAlive(true)
                .setTcpNoDelay(true)
                .build();
        cm.setDefaultSocketConfig(socketConfig);

        RequestConfig requestConfig = null;
        RequestConfig.Builder requestConfigBuild = RequestConfig.custom();
        requestConfigBuild.setConnectionRequestTimeout(timeout).setConnectTimeout(timeout).setSocketTimeout(timeout);
        if (this.proxyWare != null) {
            HttpHost proxy = new HttpHost(proxyWare.getProxyHost(), proxyWare.getProxyPort(), "http");
            requestConfigBuild.setProxy(proxy);
            requestConfig = requestConfigBuild.build();

            HttpClientBuilder httpClientBuilder = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setDefaultRequestConfig(requestConfig);
            if (proxyWare.getProxyUser() != null) {
                CredentialsProvider provider = new BasicCredentialsProvider();
                provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials(proxyWare.getProxyUser(), proxyWare.getProxyPass()));

                httpClientBuilder.setDefaultCredentialsProvider(provider);
            }
            this.httpClient = httpClientBuilder.build();
            return;
        }
        requestConfig = requestConfigBuild.build();
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig);
        this.httpClient = httpClientBuilder.build();
    }

    public String getContent(Request req) {
        try {
            if (DownloaderEnum.FOR_DOC.getValue().equals(downloaderSelect)) {
                wrap(req);
                if (req.getHeaders() != null) {
                    for (Map.Entry<String, String> en : req.getHeaders().entrySet()) {
                        getWebClient().addRequestHeader(en.getKey(), en.getValue());
                    }
                }
                if (req.getCookies() != null) {
                    URL url = null;

                    url = new URL(req.getUrl());
                    for (Map.Entry<String, String> en : req.getCookies().entrySet()) {
                        getWebClient().getCookieManager().addCookie(new Cookie(url.getHost(), en.getKey(), en.getValue()));
                    }
                }
                return ((HtmlPage) getWebClient().getPage(req.getUrl())).asXml();
            }

            InputStream stream = getBContent(req);
            if (stream != null) {
                String res = "";
                try {
                    res = IOUtils.toString(stream, req.getCharset());
                } finally {
                    stream.close();
                }
                return res;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getJsonContent(Request req) {
        return null;
    }

    public InputStream getBContent(Request req) {
        wrap(req);
        try {
            if (DownloaderEnum.FOR_DOC.getValue().equals(downloaderSelect)) {

                if (req.getHeaders() != null) {
                    //多个线程的时候有可能会造成header污染
                    for (Map.Entry<String, String> en : req.getHeaders().entrySet()) {
                        getWebClient().addRequestHeader(en.getKey(), en.getValue());
                    }
                }
                if (req.getCookies() != null) {
                    URL url = null;

                    url = new URL(req.getUrl());
                    for (Map.Entry<String, String> en : req.getCookies().entrySet()) {
                        getWebClient().getCookieManager().addCookie(new Cookie(url.getHost(), en.getKey(), en.getValue()));
                    }
                }
                return getWebClient().getPage(req.getUrl()).getWebResponse().getContentAsStream();
            } else if (DownloaderEnum.FOR_XHR.getValue().equals(downloaderSelect)) {
                HttpRequestBase httpRequestBase = null;
                if ("POST".equals(req.getMethod())) {
                    httpRequestBase = new HttpPost(req.getUrl());
                } else {
                    httpRequestBase = new HttpGet(req.getUrl());
                }
                httpRequestBase.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");

                if (req.getHeaders() != null) {
                    for (Map.Entry<String, String> en : req.getHeaders().entrySet()) {
                        httpRequestBase.setHeader(en.getKey(), en.getValue());
                    }
                }
                if (req.getCookies() != null) {
                    URL url = null;
                    url = new URL(req.getUrl());
                    StringBuilder sb = new StringBuilder();
                    sb.append("domain=" + url.getHost()).append(";");
                    sb.append("path=/").append(";");

                    for (Map.Entry<String, String> en : req.getCookies().entrySet()) {
                        sb.append(en.getKey()).append("=").append(en.getValue()).append(";");
                    }
                    httpRequestBase.setHeader("Cookie", sb.toString());
                }
                HttpResponse response = getHttpClient().execute(httpRequestBase);

                int code = response.getStatusLine().getStatusCode();
                logger.debug("responseCode=" + code);
                return response.getEntity().getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void closeClient() {
        if (getWebClient() != null) {
            getWebClient().close();
        }
        if (getHttpClient() != null) {
            try {
                getHttpClient().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object getClient() {
        if (getWebClient() != null) {
            return getWebClient();
        }
        if (getHttpClient() != null) {
            return getHttpClient();
        }
        return null;
    }

    @Override
    public void initClient() {
        if (DownloaderEnum.FOR_DOC.getValue().equals(downloaderSelect)) {
            wrapWebClient();
        } else if (DownloaderEnum.FOR_XHR.getValue().equals(downloaderSelect)) {
            try {
                wrapHttpclient();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }


    public Integer getDownloaderSelect() {
        return downloaderSelect;
    }

    public void setDownloaderSelect(Integer downloaderSelect) {
        this.downloaderSelect = downloaderSelect;
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public ProxyWare getProxyWare() {
        return proxyWare;
    }

    public void setProxyWare(ProxyWare proxyWare) {
        this.proxyWare = proxyWare;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public List<IDownloadWare> getDownloadWares() {
        return downloadWares;
    }

    public void setDownloadWares(List<IDownloadWare> downloadWares) {
        this.downloadWares = downloadWares;
    }

}
