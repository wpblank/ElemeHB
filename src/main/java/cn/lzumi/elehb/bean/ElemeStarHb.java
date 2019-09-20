package cn.lzumi.elehb.bean;

import lombok.Data;

/**
 * @author pyrde
 */
@Data
public class ElemeStarHb {
    private int id;
    private String url;
    private String caseid;
    private String sign;

    public ElemeStarHb(String url, String caseid, String sign) {
        this.url = url;
        this.caseid = caseid;
        this.sign = sign;
    }
}
