package cn.lzumi.elehb.scheduled;

import cn.lzumi.elehb.controller.ElemeController;
import cn.lzumi.elehb.controller.ElemeStarController;
import cn.lzumi.elehb.mapper.ElemeStarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时监控数据库中红包是否到达可以直接领取的状态
 *
 * @author izumi
 * @date 2019年08月12日18:00:14
 */
@Component
public class ScheduledTasks {

    @Autowired
    ElemeController elemeController;
    @Autowired
    ElemeStarController elemeStarController;
    @Autowired
    ElemeStarMapper elemeStarMapper;
    private int once = 1;
    private final Logger logger = LoggerFactory.getLogger(getClass());

//    @Scheduled(fixedDelay = 3000)
//    public int print6() {
//        logger.info("66666");
//        return 6;
//    }
//
//    @Scheduled(fixedDelay = 6000)
//    public void print7() {
//        logger.info("77777777");
//    }
}
