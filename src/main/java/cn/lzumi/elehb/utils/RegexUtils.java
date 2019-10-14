package cn.lzumi.elehb.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author izumi
 */
public class RegexUtils {

    /**
     * 根据正则匹配字符串
     *
     * @param patternStr 正则
     * @param content
     * @return
     */
    public Matcher getMatcher(String patternStr, String content) {
        Pattern pattern = Pattern.compile(patternStr);
        return pattern.matcher(content);
    }
}
