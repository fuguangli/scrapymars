package com.automata.config;

/**
 * name fuguangli
 * date 2019/12/25
 * contact businessfgl@163.com
 */
public enum ScheduleMode {
    Once(1, "run only once"),
    OneCycle(2, "run only one Cycle"),
    Always(3, "working for ever");


    private Integer value;
    private String desc;

    ScheduleMode(Integer value, String desc) {
        this.value = value;
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
