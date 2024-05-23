package com.bruce.dynamic.thread.pool.sdk.trigger.job;

import com.alibaba.fastjson.JSON;
import com.bruce.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import com.bruce.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.bruce.dynamic.thread.pool.sdk.registry.IRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

/**
 * 线程池数据上报任务
 */
public class ThreadPoolDataReportJob {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolDataReportJob.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;

    private final IRegistry registry;

    public ThreadPoolDataReportJob(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    @Scheduled(cron = "0/20 * * * * ?")
    public void execReportThreadPoolList() {
        Map<String, ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        registry.reportThreadPool(threadPoolConfigEntities);
        logger.info("动态线程池，上报线程池信息: {}", JSON.toJSONString(threadPoolConfigEntities));

        /**
         * 遍历 map 并上报详细信息
         */
        threadPoolConfigEntities.forEach((threadPoolName, threadPoolConfigEntity) -> {
            registry.reportThreadPoolConfigParameter(threadPoolConfigEntity);
            logger.info("动态线程池，上报线程池参数: {}", JSON.toJSONString(threadPoolConfigEntity));
        });

    }


}
