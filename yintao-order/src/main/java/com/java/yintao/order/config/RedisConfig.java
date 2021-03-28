package com.java.yintao.order.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author yintao
 * @desc 写清楚这个类的作用
 * @package com.java.yintao.order.config
 * @date 2021/3/2721:15
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;
//    @Value("${spring.redis.password}")
//    private String password;

    /**
     * 用于覆盖原有redisTemplate
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key 采用 string 的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // value 采用 jackson 的序列化方式
        template.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        // hash 的 key 采用 string 的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // hash 的 value 采用 jackson 的序列化方式
        template.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
