package com.bruce.dynamic.thread.pool.trigger;

import com.alibaba.fastjson.JSON;
import com.bruce.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.bruce.dynamic.thread.pool.types.Response;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/dynamic/thread/pool/")
public class DynamicThreadPoolController {

    @Resource
    public RedissonClient redissonClient;

    /**
     * 查询线程池数 list
     * curl --request GET \
     * --url 'http://localhost:8089/api/v1/dynamic/thread/pool/query_thread_pool_list'
     *
     * @return
     */
    @RequestMapping(value = "query_thread_pool_list", method = RequestMethod.GET)
    public Response<List<ThreadPoolConfigEntity>> queryThreadPoolList() {
        try{
            RMap<String, ThreadPoolConfigEntity> cacheMap = redissonClient.getMap("THREAD_POOL_CONFIG_HASH_KEY");
            List<ThreadPoolConfigEntity> cacheList = new ArrayList<>(cacheMap.values());
            return Response.<List<ThreadPoolConfigEntity>>builder()
                    .code(Response.Code.SUCCESS.getCode())
                    .info(Response.Code.SUCCESS.getInfo())
                    .data(cacheList)
                    .build();
        } catch (Exception e) {
            log.error("查询线程池数据异常", e);
            return Response.<List<ThreadPoolConfigEntity>>builder()
                    .code(Response.Code.UN_ERROR.getCode())
                    .info(Response.Code.UN_ERROR.getInfo())
                    .build();
        }
    }


    /**
     * curl --request GET \
     * --url 'http://localhost:8089/api/v1/dynamic/thread/pool/query_thread_pool_config?appName=dynamic-thread-pool-test-app&threadPoolName=threadPoolExecutor01'
     * @param appName
     * @param threadPoolName
     * @return
     */
    @RequestMapping(value = "query_thread_pool_config", method = RequestMethod.GET)
    public Response<ThreadPoolConfigEntity> queryThreadPoolConfig(@RequestParam String appName, @RequestParam String threadPoolName) {
        try{
            String cacheKey = "THREAD_POOL_CONFIG_PARAMETER_LIST_KEY" + "_" + appName + "_" + threadPoolName;
            ThreadPoolConfigEntity threadPoolConfigEntity = redissonClient.<ThreadPoolConfigEntity>getBucket(cacheKey).get();
            return Response.<ThreadPoolConfigEntity>builder()
                    .code(Response.Code.SUCCESS.getCode())
                    .info(Response.Code.SUCCESS.getInfo())
                    .data(threadPoolConfigEntity)
                    .build();

        } catch (Exception e) {
            log.error("查询线程池异常", e);
            return Response.<ThreadPoolConfigEntity>builder()
                    .code(Response.Code.UN_ERROR.getCode())
                    .info(Response.Code.UN_ERROR.getInfo())
                    .build();
        }
    }


    /**
     * curl --request POST \
     * --url 'http://localhost:8089/api/v1/dynamic/thread/pool/update_thread_pool_config' \
     * --header 'Content-Type: application/json' \
     * --data '{
     * "appName": "dynamic-thread-pool-test-app",
     * "threadPoolName": "threadPoolExecutor",
     * "corePoolSize": 20,
     * "maxPoolSize": 200,
     * }'
     * @param request
     * @return
     */
    @RequestMapping(value = "update_thread_pool_config", method = RequestMethod.POST)
    public Response<Boolean> updateThreadPoolConfig(@RequestBody ThreadPoolConfigEntity request) {
        try{
            log.info("修改线程池配置开始 {} {} {}", request.getAppName(), request.getThreadPoolName(), JSON.toJSONString(request));
            RTopic topic = redissonClient.getTopic("DYNAMIC_THREAD_POOL_REDIS_TOPIC" + "_" + request.getAppName());
            log.error("topic name {}", topic.getChannelNames());
            topic.publish(request);
            log.info("修改线程池配置完成 {} {}", request.getAppName(), request.getThreadPoolName());
            return Response.<Boolean>builder()
                    .code(Response.Code.SUCCESS.getCode())
                    .info(Response.Code.SUCCESS.getInfo())
                    .data(true)
                    .build();
        } catch (Exception e) {
            log.error("更新线程池异常", e);
            return Response.<Boolean>builder()
                    .code(Response.Code.UN_ERROR.getCode())
                    .info(Response.Code.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }
    }
}
