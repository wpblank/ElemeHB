package cn.lzumi.elehb.bean;

import lombok.Data;

/**
 * @author izumi
 */
@Data
public class ElemeHb {
    private int id;
    private String url;
    private String sn;
    private int isGet;
    private int maxNum;
    private int nowNum;
    private String getTime;

    public ElemeHb(String url,String sn, int isGet, int maxNum, int nowNum) {
        this.url = url;
        this.sn = sn;
        this.isGet = isGet;
        this.maxNum = maxNum;
        this.nowNum = nowNum;
    }
}
