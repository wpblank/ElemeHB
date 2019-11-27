package cn.lzumi.elehb.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author izumi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElemeStarHb extends Hb {
    private String caseid;
    private String sign;

    public ElemeStarHb(String url, String caseid, String sign) {
        this.url = url;
        this.caseid = caseid;
        this.sign = sign;
    }
}
