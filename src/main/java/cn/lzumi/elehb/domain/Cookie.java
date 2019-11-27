package cn.lzumi.elehb.domain;

import lombok.Data;

/**
 * @author izumi
 */
@Data
public class Cookie {
    public int id;
    /**
     * 一天内该cookie剩余使用次数
     */
    public int margin;
    /**
     * 总共领取了多少次
     */
    public int totalUse;
    /**
     * 用于判断是否失效,1为失效
     */
    public int isLose;
    /**
     * cookie 等级
     * 0:小号,每天可领取5次 1:大号兼职小号,每天可领取3次 2:大号,仅用作领取最大红包
     */
    public int level;

    public int app;

    public synchronized void use() {
        margin--;
        totalUse++;
    }
}
