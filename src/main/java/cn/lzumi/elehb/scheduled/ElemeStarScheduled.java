package cn.lzumi.elehb.scheduled;

import cn.lzumi.elehb.bean.ElemeStarCookie;
import cn.lzumi.elehb.controller.ElemeStarController;
import cn.lzumi.elehb.mapper.ElemeStarMapper;
import cn.lzumi.elehb.utils.ElemeStarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author izumi
 * @date 2019年10月09日19:55:44
 */
@Component
public class ElemeStarScheduled {
    @Autowired
    ElemeStarController elemeStarController;
    @Autowired
    ElemeStarMapper elemeStarMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "01 00 00 * * ?")
    public void clearElemeStarCookieTodayUse() {
        List<ElemeStarCookie> elemeStarCookies = elemeStarController.elemeStarCookies;
        logger.info("更新星选信息条数:{}", elemeStarMapper.updateElemeStarCookieUseInfo(elemeStarCookies));
        elemeStarController.elemeStarCookies.clear();
        logger.info("重置星选cookie今日使用次数,重置条数:{}", elemeStarMapper.clearElemeStarCookieTodayUse());
    }
}

