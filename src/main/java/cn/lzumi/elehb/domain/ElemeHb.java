package cn.lzumi.elehb.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author izumi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElemeHb extends Hb{
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
