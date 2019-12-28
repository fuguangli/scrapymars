package com.automata.downwares;

/**
 * name fuguangli
 * date 2019/12/24
 * contact businessfgl@163.com
 * 代理组件
 */
public class ProxyWare {
    private String proxyHost;
    private Integer proxyPort;

    // 代理隧道验证信息
    private String proxyUser;
    private String proxyPass;

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPass() {
        return proxyPass;
    }

    public void setProxyPass(String proxyPass) {
        this.proxyPass = proxyPass;
    }
}
