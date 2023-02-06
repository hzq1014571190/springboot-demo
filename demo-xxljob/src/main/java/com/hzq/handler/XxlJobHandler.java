package com.hzq.handler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author hzq
 * @ClassName XxlJobHandler
 * @Path com.hzq.handler.XxlJobHandler
 * @Date 2023/2/5 19:33
 * @Description
 */
@Component
public class XxlJobHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(XxlJobHandler.class);


    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("demoJobHandler")
    public void demoJobHandler() throws Exception {
        XxlJobHelper.log("XXL-JOB, Hello World.");
        LOGGER.info("我爱你");
    }
}
