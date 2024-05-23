package com.bruce.dynamic.thread.pool.sdk.registry.redis;

import com.bruce.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.bruce.dynamic.thread.pool.sdk.domain.model.valobj.RegistryEnumVO;
import com.bruce.dynamic.thread.pool.sdk.registry.IRegistry;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * redis 注册中心
 */
public class RedisRegistry implements IRegistry {

    private final RedissonClient redissonClient;

    public RedisRegistry(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void reportThreadPool(Map<String, ThreadPoolConfigEntity> threadPoolEntities) {
        RMap<String, ThreadPoolConfigEntity> map = redissonClient.getMap(RegistryEnumVO.THREAD_POOL_CONFIG_HASH_KEY.getKey());
        map.putAll(threadPoolEntities);
    }

    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String cacheKey = RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey()
                + "_"
                + threadPoolConfigEntity.getAppName()
                + "_"
                + threadPoolConfigEntity.getThreadPoolName();
        RBucket<ThreadPoolConfigEntity> bucket = redissonClient.getBucket(cacheKey);
        bucket.set(threadPoolConfigEntity, Duration.ofDays(30));

    }
}
