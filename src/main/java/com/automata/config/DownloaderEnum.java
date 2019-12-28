package com.automata.config;

/**
 * name fuguangli
 * date 2019/12/24
 * contact businessfgl@163.com
 */
public enum DownloaderEnum {
    FOR_DOC(1, "get Docs with javascript"),
    FOR_XHR(2, "any resource without javascript");

    private Integer value;
    private String desc;

    DownloaderEnum(Integer v, String desc) {
        this.value = v;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
