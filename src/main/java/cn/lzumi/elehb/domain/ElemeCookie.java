package cn.lzumi.elehb.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author izumi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElemeCookie extends Cookie{
    private String openId;
    private String sid;
    private String sign;
    private String weixinAvatar;
    private String weixinUsername;
}
