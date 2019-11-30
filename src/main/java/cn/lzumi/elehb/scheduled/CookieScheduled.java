package cn.lzumi.elehb.scheduled;

import cn.lzumi.elehb.mapper.CookieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时刷新cookie
 *
 * @author izumi
 */
@Component
public class CookieScheduled {
    @Autowired
    CookieMapper cookieMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "00 00 00 * * ?")
    public void resetMargin() {
        logger.info("今日重置饿了么星选cookie条数:{}", cookieMapper.resetMargin());
    }
}
