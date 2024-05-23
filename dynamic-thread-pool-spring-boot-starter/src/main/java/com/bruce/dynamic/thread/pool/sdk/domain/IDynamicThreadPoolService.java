package com.bruce.dynamic.thread.pool.sdk.domain;

import com.bruce.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;
import java.util.Map;

/**
 * 动态线程池服务
 */
public interface IDynamicThreadPoolService {
    Map<String, ThreadPoolConfigEntity> queryThreadPoolList();

    ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName);

    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);
}
