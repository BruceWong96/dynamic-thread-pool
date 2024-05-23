package com.bruce.dynamic.thread.pool.sdk.registry;

import com.bruce.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;
import java.util.Map;

/**
 * 注册中心接口
 */
public interface IRegistry {

    void reportThreadPool(Map<String,ThreadPoolConfigEntity> threadPoolEntities);

    void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity);
}
